package com.bgrem.presentation.background.select.model

interface SelectBackgroundListener {
    fun onSelectBackPressed()
    fun onBackgroundSelected(backgroundId: String?)
    fun onTransparentSelected()
}