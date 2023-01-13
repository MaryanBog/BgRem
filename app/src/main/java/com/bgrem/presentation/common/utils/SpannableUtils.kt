package com.bgrem.presentation.common.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.SuperscriptSpan
import android.view.View
import androidx.annotation.ColorInt

object SpannableUtils {
    fun changeTypefaceOfSubstring(
        typeface: Int,
        text: String,
        subText: List<String>,
        ignoreCase: Boolean = true
    ): SpannableString {
        val spannableString = SpannableString(text)
        if (text.isBlank() || subText.isEmpty()) return spannableString

        subText.forEach { sub ->
            val start = text.indexOf(sub, ignoreCase = ignoreCase)
            val end = start + sub.length

            if (start > -1) {
                spannableString.setSpan(
                    StyleSpan(typeface),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableString
    }

    fun makeTextWithClickablePart(
        text: String,
        clickableParts: List<Pair<String, (() -> Unit)?>>,
        isUnderlinedClickableParts: Boolean = false,
        @ColorInt
        clickablePartColor: Int? = null,
    ): SpannableString {
        val spannableString = SpannableString(text)

        clickableParts.forEach { clickablePart ->
            val clickableText = clickablePart.first
            val startIndex = text.indexOf(clickableText)
            if (startIndex == -1) return@forEach

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    clickablePart.second?.invoke()
                }

                override fun updateDrawState(textPaint: TextPaint) {
                    super.updateDrawState(textPaint)
                    textPaint.isUnderlineText = isUnderlinedClickableParts
                }
            }

            spannableString.setSpan(
                clickableSpan,
                startIndex,
                startIndex + clickableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            clickablePartColor?.let {
                spannableString.setSpan(
                    ForegroundColorSpan(it),
                    startIndex,
                    startIndex + clickableText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }
}