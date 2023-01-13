package com.bgrem.presentation.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bgrem.app.R
import com.bgrem.app.databinding.ViewAppNameWithLogoBinding
import com.bgrem.presentation.common.utils.SpannableUtils

class AppNameWithLogoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAppNameWithLogoBinding by viewBinding()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_name_with_logo, this, true)
        initView()
    }

    private fun initView() = with(binding) {
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL

        appNameText.text = SpannableUtils.changeTypefaceOfSubstring(
            typeface = Typeface.BOLD,
            text = context.getString(R.string.app_name),
            subText = listOf(context.getString(R.string.app_name_bg))
        )
    }
}