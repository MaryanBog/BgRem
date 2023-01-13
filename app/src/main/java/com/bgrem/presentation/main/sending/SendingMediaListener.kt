package com.bgrem.presentation.main.sending

import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task

interface SendingMediaListener {
    fun onMediaSent(task: Task, mediaType: MediaType)
}