package com.igor_shaula.api_polling.ui_layer.list_ui

import android.widget.TextView
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class DynamicDotsProvider(private val textView: TextView) {

    private val textDotsCoroutineScope: CoroutineScope =
        MainScope() + CoroutineName("textDotsCoroutineScope")
//    private val textDotsCoroutineScope = CoroutineScope(Dispatchers.Main + Job()) // doesn't work

    private var textAnimationJob: Job? = null
    private var textBeforeAnimation: String = ""

    fun startShowing5DynamicDots() {
        textBeforeAnimation = textView.text.toString()
        val baseChar = '.'
        val highChar = '|'
        val separator = ' '
        val textLength = textView.text.length
        val animationFrameTime = 1000 / textLength
        val text0 = ". . . . ."
        val text1 = "| . . . ."
        val text2 = "| | . . ."
        val text3 = "| | | . ."
        val text4 = "| | | | ."
        val text5 = "| | | | |"
        textAnimationJob = (GlobalScope + CoroutineName(textView.text.toString()))
            .launch { // works fine but it's dangerous API
//        textDotsCoroutineScope.launch { // text is not updated for unknown reason
                while (isActive) {
                    textView.text = text0
                    delay(100)
                    textView.text = text1
                    delay(100)
                    textView.text = text2
                    delay(100)
                    textView.text = text3
                    delay(100)
                    textView.text = text4
                    delay(100)
                    textView.text = text5
                    delay(500)
                }
            }
    }

    fun stopShowingDynamicDottedText() {
        if (textDotsCoroutineScope.isActive)
            textDotsCoroutineScope.cancel()
        textAnimationJob?.cancel()
        if (textBeforeAnimation.isNotEmpty())
            textView.text = textBeforeAnimation
    }
}