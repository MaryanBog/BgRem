package com.compose.presentation.gallery.video

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.compose.data.models.GetImageTypesParams
import com.compose.utils.Constant

class OpenVideoContract: ActivityResultContract<GetImageTypesParams, Uri?>() {
    override fun createIntent(context: Context, input: GetImageTypesParams): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(Constant.VIDEO_ALL)
            .putExtra(Intent.EXTRA_MIME_TYPES, input.mimeTypes.toTypedArray())
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}