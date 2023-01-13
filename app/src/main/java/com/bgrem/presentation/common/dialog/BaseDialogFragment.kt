package com.bgrem.presentation.common.dialog

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import com.bgrem.app.R

abstract class BaseDialogFragment(@LayoutRes layoutRes: Int): DialogFragment(layoutRes) {

    @StyleRes
    protected open val themeRes: Int = R.style.AppMaterialDialog_FullScreen
    protected open val listener: DialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, themeRes)
    }

    protected open fun onPositiveClick() = dismiss()
    protected open fun onNegativeClick() = dismiss()

    interface DialogListener
}