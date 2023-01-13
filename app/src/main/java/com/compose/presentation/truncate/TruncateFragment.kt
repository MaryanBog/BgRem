package com.compose.presentation.truncate

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bgrem.presentation.trimming.thumbnail.ThumbnailFragment

class TruncateFragment: Fragment() {

    companion object {
        private const val KEY_URI_THUMB = "KEY_URI_THUMB"
        private const val MAX_DURATION = 29
        private const val MIN_DURATION = 0
        private const val DELAY_DELETE = 1000L
        private const val TIME_MILLIS = 2000L
        private const val PROGRESS_MILLIS = 3500L

    }
}