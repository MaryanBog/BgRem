package com.bgrem.presentation.common.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.loadImageByUrl(url: String) = Glide
    .with(this)
    .load(url)
    .into(this)

fun ImageView.loadImageByUrlWithRoundedCorners(url: String, radius: Int) = Glide
    .with(this)
    .load(url)
    .apply(RequestOptions().transform(RoundedCorners(radius)))
    .into(this)

fun ImageView.loadGifDrawable(
    @DrawableRes drawableRes: Int,
    loopCount: Int = GifDrawable.LOOP_FOREVER,
    onGifEnd: (() -> Unit)? = null,
) = Glide
    .with(this)
    .asGif()
    .load(drawableRes)
    .listener(object : RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>?,
            isFirstResource: Boolean,
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            resource.setLoopCount(loopCount)
            resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    onGifEnd?.invoke()
                }
            })
            return false
        }
    })
    .into(this)