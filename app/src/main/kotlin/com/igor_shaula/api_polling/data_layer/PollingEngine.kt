package com.igor_shaula.api_polling.data_layer

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface PollingEngine {

    fun launch(theWorkToDo: () -> Unit, intervalInSeconds: Int)

    fun stopAndClearItself()
}

class JavaTPEBasedPollingEngine : PollingEngine {

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    fun prepare(size: Int) {
        if (scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor?.isShutdown == true) {
            scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(size)
        }
    }

    override fun launch(theWorkToDo: () -> Unit, intervalInSeconds: Int) {
        scheduledThreadPoolExecutor?.scheduleAtFixedRate(
            theWorkToDo, 0, 5, TimeUnit.SECONDS
        )
    }

    override fun stopAndClearItself() {
        scheduledThreadPoolExecutor?.shutdown()
    }
}