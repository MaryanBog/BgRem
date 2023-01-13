package com.compose.data

import android.content.SharedPreferences
import com.bgrem.domain.common.media.MediaType
import java.io.File

interface SharedPrefStorage {
    fun saveVideoIds(text: Set<String>)
    fun getVideoIds(): Set<String>?

    fun saveImageIds(text: Set<String>)
    fun getImageIds(): Set<String>?

    fun saveStateWelcomeScreen(showing: Boolean)
    fun getStateWelcomeScreen(): Boolean

    fun registerSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener)
}