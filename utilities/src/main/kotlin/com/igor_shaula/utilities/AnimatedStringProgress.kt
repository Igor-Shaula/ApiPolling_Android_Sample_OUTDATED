package com.igor_shaula.utilities

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

private const val BASE_CHAR = ':'
private const val HIGH_CHAR = '|'
private const val TOTAL_ANIMATION_TIME = 1000L // 1 second

class AnimatedStringProgress(private val textView: TextView) {

    private val textDotsCoroutineScope: CoroutineScope =
        MainScope() + CoroutineName(this.javaClass.simpleName)
//    private val textDotsCoroutineScope = CoroutineScope(Dispatchers.Main + Job()) // doesn't work

    private var textAnimationJob: Job? = null
    private var textBeforeAnimation: String = ""
    private var textColorBeforeAnimation: Int = Color.BLACK // in fact this value is never used
    private var textTypefaceBeforeAnimation: Typeface? = null
    private val stringBuilder = StringBuilder()
    private var nextChar: Char = BASE_CHAR

    @OptIn(DelicateCoroutinesApi::class)
    fun startShowingDynamicDottedText() {
        textBeforeAnimation = textView.text.toString()
        textColorBeforeAnimation = textView.currentTextColor
        textTypefaceBeforeAnimation = textView.typeface
        val textLength = textView.text.length
        val animationFrameTime = TOTAL_ANIMATION_TIME / textLength
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.setTextColor(Color.MAGENTA)
//        textDotsCoroutineScope.launch { // text is not updated for unknown reason
        textAnimationJob = (GlobalScope + CoroutineName(textView.text.toString()))
            .launch { // works fine but it's dangerous API
                var indexOfStep = 0
                while (isActive) {
                    repeat(textLength) {
                        withContext(Dispatchers.Main) {
                            textView.text = getStringForThisTick(indexOfStep)
                        }
                        delay(animationFrameTime)
                        if (indexOfStep < textLength) indexOfStep++
                        else indexOfStep = 0
                    }
                }
            }
    }

    // REMEMBER TO INVOKE THIS METHOD - otherwise there will be a memory leak with textAnimationJob
    fun stopShowingDynamicDottedText() {

        // at first we need to stop everything & clear resources
        if (textDotsCoroutineScope.isActive)
            textDotsCoroutineScope.cancel()
        textAnimationJob?.cancel()
        textAnimationJob = null

        // now obviously it will be nice to restore initial text in the given view
        if (textBeforeAnimation.isNotEmpty()) {
            textView.text = textBeforeAnimation
        }

        // restoring initial text settings - not relative to text length
        textView.typeface = textTypefaceBeforeAnimation
        textView.setTextColor(textColorBeforeAnimation)
    }

    private fun getStringForThisTick(indexOfStep: Int): String {
        stringBuilder.clear()
        for (i in textBeforeAnimation.indices) {
            nextChar = if (i <= indexOfStep) HIGH_CHAR else BASE_CHAR
            stringBuilder.append(nextChar)
        }
        return stringBuilder.toString()
    }
}
