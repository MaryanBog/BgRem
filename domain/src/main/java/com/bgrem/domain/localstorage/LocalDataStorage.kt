package com.bgrem.domain.localstorage

interface LocalDataStorage {
    suspend fun clearAll()

    suspend fun setFirstLaunched()
    suspend fun getIsFirstLaunch(): Boolean

    suspend fun setCurrentJobId(jobId: String?)
    suspend fun getCurrentJobId(): String?

    suspend fun setIsPortrait(isPortrait: Boolean)
    suspend fun getIsPortrait(): Boolean
}