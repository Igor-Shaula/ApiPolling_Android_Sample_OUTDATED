package com.igor_shaula.api_polling.data_layer.polling_engines

interface PollingEngine {

    fun launch(intervalInSeconds: Int, theWorkToDo: () -> Unit)

    fun stopAndClearItself()
}
