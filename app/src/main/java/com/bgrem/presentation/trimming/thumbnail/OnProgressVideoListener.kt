package com.bgrem.presentation.trimming.thumbnail

interface OnProgressVideoListener {
    fun updateProgress(time: Float, max: Float, scale: Float)
}