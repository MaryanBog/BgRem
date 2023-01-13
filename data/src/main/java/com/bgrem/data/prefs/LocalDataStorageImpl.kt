package com.bgrem.data.prefs

import android.content.SharedPreferences
import com.bgrem.domain.localstorage.LocalDataStorage

class LocalDataStorageImpl(
    private val prefs: SharedPreferences
) : LocalDataStorage {

    companion object {
        // While adding new keys, add them to clearAll() method
        private const val KEY_IS_FIRST_LAUNCH = "KEY_IS_FIRST_LAUNCH"
        private const val KEY_CURRENT_JOB_ID = "KEY_CURRENT_JOB_ID"
        private const val KEY_IS_PORTRAIT = "KEY_IS_PORTRAIT"
    }

    override suspend fun clearAll() {
        removeValue(KEY_IS_FIRST_LAUNCH)
        removeValue(KEY_CURRENT_JOB_ID)
    }

    override suspend fun setFirstLaunched() {
        prefs.edit().putBoolean(KEY_IS_FIRST_LAUNCH, false).apply()
    }

    override suspend fun getIsFirstLaunch(): Boolean =
        prefs.getBoolean(KEY_IS_FIRST_LAUNCH, true)

    override suspend fun setCurrentJobId(jobId: String?) {
        prefs.edit().putString(KEY_CURRENT_JOB_ID, jobId).apply()
    }

    override suspend fun getCurrentJobId(): String? {
        return prefs.getString(KEY_CURRENT_JOB_ID, null)
    }

    override suspend fun setIsPortrait(isPortrait: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PORTRAIT, isPortrait).apply()
    }

    override suspend fun getIsPortrait(): Boolean {
        return prefs.getBoolean(KEY_IS_PORTRAIT, false)
    }

    private fun removeValue(key: String) = prefs.edit().remove(key).apply()
}