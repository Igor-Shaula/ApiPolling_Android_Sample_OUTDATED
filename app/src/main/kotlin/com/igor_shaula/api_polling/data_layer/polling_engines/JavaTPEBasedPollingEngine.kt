package com.igor_shaula.api_polling.data_layer.polling_engines

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class JavaTPEBasedPollingEngine @Inject constructor(size: Int) : PollingEngine {

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    init {
        scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(size)
    }

    override fun launch(intervalInSeconds: Int, theWorkToDo: () -> Unit) {
        scheduledThreadPoolExecutor?.scheduleAtFixedRate(
            theWorkToDo, 0, 5, TimeUnit.SECONDS
        )
    }

    override fun stopAndClearItself() {
        scheduledThreadPoolExecutor?.shutdown()
    }

    companion object {

        private var thisPollingEngine: JavaTPEBasedPollingEngine? = null

        fun prepare(size: Int): JavaTPEBasedPollingEngine {
            thisPollingEngine = JavaTPEBasedPollingEngine(size)
            return thisPollingEngine as JavaTPEBasedPollingEngine
        }
    }
}
