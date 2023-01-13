package com.compose.di

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.core.os.bundleOf
import androidx.navigation.navArgument
import com.compose.data.FileStorages
import com.compose.data.RepositoryBgRem
import com.compose.data.SharedPrefStorage
import com.compose.data.models.MediaInfo
import com.compose.data.repository.RepositoryBgRemImpl
import com.compose.data.storage.FileStoragesImpl
import com.compose.data.storage.SharedPrefStorageImpl
import com.compose.utils.Constant
import com.compose.utils.MimeTypesUtils
import com.compose.utils.MimeTypesUtilsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.core.parameter.parametersOf
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPrefStorage(@ApplicationContext context: Context): SharedPrefStorage {
        return SharedPrefStorageImpl(context)
    }

    @Singleton
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver{
        return context.contentResolver
    }

    @Singleton
    @Provides
    fun provideDirectory(@ApplicationContext context: Context): File?{
        return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
    }

    @Singleton
    @Provides
    fun provideRepositoryBgRem(
        sharedPrefStorage: SharedPrefStorage
    ): RepositoryBgRem {
        return RepositoryBgRemImpl(sharedPrefStorage)
    }

    @Singleton
    @Provides
    fun provideMimeTypesUtils(@ApplicationContext context: Context): MimeTypesUtils {
        return MimeTypesUtilsImpl(context)
    }

    @Singleton
    @Provides
    fun provideFileStorages(@ApplicationContext context: Context): FileStorages {
        return FileStoragesImpl(context.cacheDir)
    }
}