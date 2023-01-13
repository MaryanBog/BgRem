package com.bgrem.presentation.common.utils

import android.content.Intent
import android.net.Uri

object IntentUtils {
    private const val TEXT_MIME_TYPE = "text/plain"

    fun getBrowserIntent(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    fun getSendTextIntent(message: String): Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = TEXT_MIME_TYPE
    }

    fun getEmailSendIntent(email: String) = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(
            Intent.EXTRA_EMAIL,
            listOf(email).toTypedArray()
        )
    }

    fun getShareMediaIntent(mediaUri: Uri, mimeType: String): Intent =
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, mediaUri)
            type = mimeType
        }
}