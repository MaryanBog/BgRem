package com.bgrem.presentation.media

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.bgrem.domain.common.media.MediaType

class CaptureMediaContract: ActivityResultContract<MediaType, Uri?>() {
    override fun createIntent(context: Context, input: MediaType): Intent {
        return CaptureMediaActivity.newIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}