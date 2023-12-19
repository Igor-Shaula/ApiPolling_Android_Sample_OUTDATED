package com.igor_shaula.api_polling.data_layer

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface PollingEngine {

    fun launch(intervalInSeconds: Int, theWorkToDo: () -> Unit)

    fun stopAndClearItself()
}

class JavaTPEBasedPollingEngine : PollingEngine {

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    fun prepare(size: Int) {
        if (scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor?.isShutdown == true) {
            scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(size)
        }
    }

    override fun launch(intervalInSeconds: Int, theWorkToDo: () -> Unit) {
        scheduledThreadPoolExecutor?.scheduleAtFixedRate(
            theWorkToDo, 0, 5, TimeUnit.SECONDS
        )
    }

    override fun stopAndClearItself() {
        scheduledThreadPoolExecutor?.shutdown()
//        scheduledThreadPoolExecutor?.purge() // todo find out if this is needed here
        scheduledThreadPoolExecutor = null
    }

    companion object {

        private var thisPollingEngine: JavaTPEBasedPollingEngine? = null

        fun prepare(size: Int): JavaTPEBasedPollingEngine {
            if (thisPollingEngine == null) {
                thisPollingEngine = JavaTPEBasedPollingEngine()
                thisPollingEngine?.prepare(size)
            }
            return thisPollingEngine as JavaTPEBasedPollingEngine
        }
    }
}