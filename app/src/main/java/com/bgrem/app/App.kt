package com.bgrem.app

import android.app.Application
import com.bgrem.app.di.KoinModules
import com.bgrem.app.log.ReleaseTree
import com.bgrem.domain.background.ClearBackgroundsUseCase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import timber.log.Timber

class App : Application() {
    private val appScope by inject<CoroutineScope>(named(KoinModules.KEY_APP_SCOPE))
    private val clearBackgroundsUseCase by inject<ClearBackgroundsUseCase>()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        setCrashlyticsCollection()
        initKoin()
        initTimber()
        clearBackgrounds()
        firebaseAnalytics = Firebase.analytics
    }

    private fun setCrashlyticsCollection(){
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(KoinModules.all)
        }
    }

    private fun initTimber() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
    }

    private fun clearBackgrounds() {
        appScope.launch {
            clearBackgroundsUseCase()
        }
    }
}