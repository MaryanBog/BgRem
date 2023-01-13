package com.bgrem.domain.background

import com.bgrem.domain.background.model.Background
import java.io.File

interface BackgroundNetworkDataSource {
    suspend fun getAllBackgrounds(): List<Background>
    suspend fun createBackground(file: File, mimeType: String): Background
}