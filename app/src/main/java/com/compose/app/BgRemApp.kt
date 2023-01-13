package com.compose.app

import android.app.Application
import com.bgrem.app.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BgRemApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setCrashlyticsCollection()
    }

    private fun setCrashlyticsCollection(){
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}