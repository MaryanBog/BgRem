package com.bgrem.domain.background

import com.bgrem.domain.background.model.Background
import kotlinx.coroutines.flow.Flow

interface BackgroundDbDataSource {
    fun observeBackgrounds(): Flow<List<Background>>
    suspend fun getUserBackgrounds(): List<Background>

    suspend fun addBackground(background: Background)
    suspend fun addBackgrounds(backgrounds: List<Background>)

    suspend fun clearAll()
}