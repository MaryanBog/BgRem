package com.bgrem.data.db.background

import com.bgrem.data.db.background.model.BackgroundEntity
import com.bgrem.domain.background.BackgroundDbDataSource
import com.bgrem.domain.background.model.Background
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackgroundDbDataSourceImpl(
    private val backgroundDao: BackgroundDao
) : BackgroundDbDataSource {

    override fun observeBackgrounds(): Flow<List<Background>> =
        backgroundDao.observeBackgrounds().map { data -> data.map { it.toDomain() } }

    override suspend fun getUserBackgrounds(): List<Background> {
        return backgroundDao.getUserBackgrounds().map { it.toDomain() }
    }

    override suspend fun addBackground(background: Background) {
        backgroundDao.addBackground(BackgroundEntity.fromDomain(background))
    }

    override suspend fun addBackgrounds(backgrounds: List<Background>) {
        backgroundDao.addBackgrounds(backgrounds.map { BackgroundEntity.fromDomain(it) })
    }

    override suspend fun clearAll() {
        backgroundDao.clearAll()
    }
}