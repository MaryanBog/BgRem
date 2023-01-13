package com.bgrem.presentation.fullScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bgrem.app.R
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.fullScreen.video.VideoPreviewFragment
import com.bgrem.presentation.fullScreen.image.ZoomImageFragment
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.replaceFragment

class FullScreenActivity:
    AppCompatActivity(R.layout.activity_capture_media)
{

    private val mediaType by lazy { getExtra<MediaType>(KEY_MEDIA_TYPE) }
    private val uriString by lazy { getExtra<String>(KEY_URI) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (mediaType) {
            MediaType.VIDEO -> showVideoPreviewFragment(uriString)
            MediaType.IMAGE -> showZoomImageFragment(uriString)
            else -> {}
        }
    }

    private fun showVideoPreviewFragment(uri: String?) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = VideoPreviewFragment.TAG
    ) { VideoPreviewFragment.newInstance(uri) }

    private fun showZoomImageFragment(uri: String?) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = ZoomImageFragment.TAG
    ) { ZoomImageFragment.newInstance(uri)}

    companion object {
        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"
        private const val KEY_URI = "KEY_URI"

        fun newIntent(context: Context, mediaType: MediaType, uri: String) =
            Intent(context, FullScreenActivity::class.java)
                .putExtra(KEY_MEDIA_TYPE, mediaType)
                .putExtra(KEY_URI, uri)
    }
}