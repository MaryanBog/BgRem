package com.bgrem.presentation.common.dialog

import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.bgrem.app.R

abstract class FullScreenDialog(@LayoutRes layoutRes: Int): BaseDialogFragment(layoutRes) {

    @StyleRes
    override val themeRes: Int = R.style.AppMaterialDialog_FullScreen
}