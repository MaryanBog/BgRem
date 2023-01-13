package com.bgrem.presentation.common.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.common.AppConstants

data class GetContentByMimeTypesParams(
    val type: MediaType,
    val mimeTypes: List<String>
)

open class OpenDocumentByMimeTypes : ActivityResultContract<GetContentByMimeTypesParams, Uri?>() {
    override fun createIntent(context: Context, input: GetContentByMimeTypesParams): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input.type.toPickerTypes())
            .putExtra(Intent.EXTRA_MIME_TYPES, input.mimeTypes.toTypedArray())
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }

    private fun MediaType.toPickerTypes() = when (this) {
        MediaType.VIDEO -> AppConstants.VIDEO_ALL
        MediaType.IMAGE -> AppConstants.IMAGE_ALL
    }
}