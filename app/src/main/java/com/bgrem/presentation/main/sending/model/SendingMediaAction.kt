package com.bgrem.presentation.main.sending.model

import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task

sealed class SendingMediaAction {
    data class MediaSent(val task: Task, val mediaType: MediaType) : SendingMediaAction()
}
