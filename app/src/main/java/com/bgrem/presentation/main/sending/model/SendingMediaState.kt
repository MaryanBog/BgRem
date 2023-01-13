package com.bgrem.presentation.main.sending.model

data class SendingMediaState(
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
