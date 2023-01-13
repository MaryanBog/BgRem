package com.bgrem.presentation.common.utils

import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

object SnackBarUtils {
    fun showSnack(
        view: View,
        message: String,
        @ColorInt bgColor: Int,
        @ColorInt textColor: Int,
        durationMs: Int = Snackbar.LENGTH_LONG
    ) {
        Snackbar.make(view, message, durationMs)
            .setBackgroundTint(bgColor)
            .setTextColor(textColor)
            .show()
    }
}