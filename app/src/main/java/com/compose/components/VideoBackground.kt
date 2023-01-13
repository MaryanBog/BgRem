package com.compose.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.ResizeMode
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoBackground(videoBgUrl: String) {

    val context = LocalContext.current

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            this.clearMediaItems()
        }
    }

    val mediaItem = MediaItem.Builder()
        .setUri(videoBgUrl)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .build()
        )
        .build()

    exoPlayer.apply {
        this.setMediaItem(mediaItem)
        this.prepare()
        this.playWhenReady = true
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        DisposableEffect(
            AndroidView(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(Alignment.Center),
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = RESIZE_MODE_FILL
                    }
                }
            )
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }

}