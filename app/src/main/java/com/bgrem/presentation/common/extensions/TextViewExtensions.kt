package com.bgrem.presentation.common.extensions

import android.animation.ValueAnimator
import android.widget.TextView

private const val POINT = '.'
private const val SPACE = ' '

fun TextView.animateEndsDots(maxDotsCount: Int, durationInMillis: Long) {
    val pointValues = (0..maxDotsCount + 1).toList().toIntArray()
    ValueAnimator.ofInt(*pointValues).apply {
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        duration = durationInMillis
        addUpdateListener {
            val pointCount = it.animatedValue as Int
            val clearText = text.toString().trimEnd(POINT, SPACE)
            val points = POINT.toString().repeat(pointCount)
            val spaces = SPACE.toString().repeat(maxDotsCount - pointCount + 1)
            text = clearText + points + spaces
        }
    }.start()
}