package com.compose.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.compose.data.SharedPrefStorage
import javax.inject.Inject

class SharedPrefStorageImpl @Inject constructor(
    private val context: Context
) : SharedPrefStorage {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun saveVideoIds(text: Set<String>) {
        sharedPref
            .edit()
            .putStringSet(KEY_SAVE_VIDEO, text)
            .apply()
    }

    override fun saveStateWelcomeScreen(showing: Boolean) {
        sharedPref
            .edit()
            .putBoolean(KEY_SHOW_WELCOME_SCREEN, showing)
            .apply()
    }

    override fun getStateWelcomeScreen(): Boolean {
        return sharedPref.getBoolean(KEY_SHOW_WELCOME_SCREEN, true)
    }

    override fun getVideoIds(): Set<String>? {
        return sharedPref.getStringSet(KEY_SAVE_VIDEO, setOf())
    }

    override fun saveImageIds(text: Set<String>) {
        sharedPref
            .edit()
            .putStringSet(KEY_SAVE_IMAGE, text)
            .apply()
    }

    override fun getImageIds(): Set<String>? {
        return sharedPref.getStringSet(KEY_SAVE_IMAGE, setOf())
    }

    override fun registerSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPref.registerOnSharedPreferenceChangeListener(changeListener)
    }

    override fun unregisterSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPref.unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        const val SHARED_PREF_NAME = "shared_pref_name"
        const val KEY_SAVE_IMAGE = "key_save_image"
        const val KEY_SAVE_VIDEO = "key_save_video"
        const val KEY_SHOW_WELCOME_SCREEN = "key_show_welcome_screen"
    }
}