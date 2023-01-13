package com.bgrem.presentation.common.dialog

import com.bgrem.app.R

class BlockingProgressDialog : FullScreenDialog(R.layout.dialog_blocking_progress) {
    companion object {
        val TAG: String = BlockingProgressDialog::class.java.simpleName

        fun create() = BlockingProgressDialog().apply {
            isCancelable = false
        }
    }

    override val themeRes: Int = R.style.AppMaterialDialog_FullScreen_Progress
}