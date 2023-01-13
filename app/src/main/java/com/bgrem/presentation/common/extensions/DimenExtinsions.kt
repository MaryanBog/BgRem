package com.bgrem.presentation.common.extensions

import android.content.res.Resources

val Int.dpToPx
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float
    get() = this * Resources.getSystem().displayMetrics.density