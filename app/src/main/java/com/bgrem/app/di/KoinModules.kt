package com.bgrem.app.di

import android.content.ContentResolver
import android.content.Context
import android.os.Environment
import com.bgrem.app.BuildConfig
import com.bgrem.data.db.background.BackgroundDbDataSourceImpl
import com.bgrem.data.db.common.AppDatabase
import com.bgrem.data.db.common.Database
import com.bgrem.data.files.FileStorageImpl
import com.bgrem.data.net.background.BackgroundApi
import com.bgrem.data.net.background.BackgroundNetworkDataSourceImpl
import com.bgrem.data.net.common.Network
import com.bgrem.data.net.common.failure.FailureInterceptor
import com.bgrem.data.net.common.failure.StatusCodesHandler
import com.bgrem.data.net.common.failure.StatusCodesHandlerImpl
import com.bgrem.data.net.job.JobApi
import com.bgrem.data.net.job.JobDataSourceImpl
import com.bgrem.data.net.task.TaskApi
import com.bgrem.data.net.task.TaskDataSourceImpl
import com.bgrem.data.prefs.LocalDataStorageImpl
import com.bgrem.domain.background.*
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.job.*
import com.bgrem.domain.launch.GetIsFirstLaunchUseCase
import com.bgrem.domain.launch.GetIsFirstLaunchUseCaseImpl
import com.bgrem.domain.launch.SetFirstLaunchedUseCase
import com.bgrem.domain.launch.SetFirstLaunchedUseCaseImpl
import com.bgrem.domain.localstorage.LocalDataStorage
import com.bgrem.domain.task.*
import com.bgrem.managers.MimeTypeManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.Executors

object KoinModules {
    private const val KEY_CACHE_DIRECTORY = "KEY_CACHE_DIRECTORY"
    const val KEY_DCIM_DIRECTORY = "KEY_DCIM_DIRECTORY"

    const val KEY_APP_SCOPE = "KEY_APP_SCOPE"

    private const val KEY_FAILURE_INTERCEPTOR = "KEY_FAILURE_INTERCEPTOR"

    private val utilModule = module {
        single(named(KEY_APP_SCOPE)) {
            CoroutineScope(
                SupervisorJob() + Executors.newCachedThreadPool().asCoroutineDispatcher()
            )
        }
        factory<ContentResolver> { androidContext().contentResolver }
    }

    private val prefsModule = module {
        single {
            androidContext().getSharedPreferences(
                BuildConfig.PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
        single<LocalDataStorage> { LocalDataStorageImpl(prefs = get()) }
    }

    private val databaseModule = module {
        single { Database.build(androidContext()) }
        single { get<AppDatabase>().getBackgroundDao() }
    }

    private val fileModule = module {
        single(named(KEY_CACHE_DIRECTORY)) { androidContext().cacheDir }
        single(named(KEY_DCIM_DIRECTORY)) { androidContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) }
        single<FileStorage> { FileStorageImpl(cacheDir = get(named(KEY_CACHE_DIRECTORY))) }
        single<MimeTypeManager> { MimeTypeManagerImpl(context = androidContext()) }
    }

    private val dataSourceModule = module {
        single<JobDataSource> { JobDataSourceImpl(jobApi = get()) }
        single<TaskDataSource> { TaskDataSourceImpl(taskApi = get()) }
        single<BackgroundNetworkDataSource> { BackgroundNetworkDataSourceImpl(backgroundApi = get()) }
        single<BackgroundDbDataSource> { BackgroundDbDataSourceImpl(backgroundDao = get()) }
    }

    private val useCaseModule = module {
        factory<GetIsFirstLaunchUseCase> { GetIsFirstLaunchUseCaseImpl(localDataStorage = get()) }
        factory<SetFirstLaunchedUseCase> { SetFirstLaunchedUseCaseImpl(localDataStorage = get()) }
        factory<CreateJobAndRemoveBackgroundUseCase> {
            CreateJobAndRemoveBackgroundUseCaseImpl(
                fileStorage = get(),
                jobDataSource = get(),
                taskDataSource = get(),
                localDataStorage = get()
            )
        }
        factory<GetTaskUseCase> { GetTaskUseCaseImpl(taskDataSource = get()) }
        factory<ObserveBackgroundsUseCase> {
            ObserveBackgroundsUseCaseImpl(
                backgroundDbDataSource = get(),
                backgroundNetworkDataSource = get()
            )
        }
        factory<ClearBackgroundsUseCase> {
            ClearBackgroundsUseCaseImpl(
                backgroundDbDataSource = get(),
                fileStorage = get()
            )
        }
        factory<CreateBackgroundUseCase> {
            CreateBackgroundUseCaseImpl(
                backgroundDbDataSource = get(),
                backgroundNetworkDataSource = get(),
                fileStorage = get()
            )
        }
        factory<DownloadResultFileUseCase> {
            DownloadResultFileUseCaseImpl(
                fileStorage = get(),
                taskDataSource = get(),
                mimeTypeManager = get(),
                directory = get(named(KEY_DCIM_DIRECTORY))
            )
        }
        factory<ClearJobUseCase> {
            ClearJobUseCaseImpl(
                fileStorage = get(),
                localDataStorage = get(),
                contentDirectory = get(named(KEY_DCIM_DIRECTORY))
            )
        }
        factory<GetIsPortraitMediaUseCase> { GetIsPortraitMediaUseCaseImpl(localDataStorage = get()) }
    }

    private val interactorModule = module {
        factory<RemoveBackgroundInteractor> {
            RemoveBackgroundInteractorImpl(
                localDataStorage = get(),
                taskDataSource = get()
            )
        }
    }

    private val networkModule = module {
        single { Network.appJson }
        single<StatusCodesHandler> { StatusCodesHandlerImpl() }
        single<Interceptor>(named(KEY_FAILURE_INTERCEPTOR)) { FailureInterceptor(statusCodesHandler = get()) }
        single {
            Network.getHttpClient(
                isDebug = BuildConfig.DEBUG,
                failureInterceptor = get(named(KEY_FAILURE_INTERCEPTOR))
            )
        }
        single {
            Network.getRetrofit(
                baseUrl = BuildConfig.API_URL,
                json = get(),
                httpClient = get()
            )
        }
    }

    private val apiModule = module {
        single<JobApi> { Network.getApi(retrofit = get()) }
        single<TaskApi> { Network.getApi(retrofit = get()) }
        single<BackgroundApi> { Network.getApi(retrofit = get()) }
    }

    private val baseAppModule = module {
        loadKoinModules(
            listOf(
                dataSourceModule,
                useCaseModule,
                interactorModule
            )
        )
    }
    val all = listOf(
        baseAppModule,
        presentationModule,
        networkModule,
        apiModule,
        prefsModule,
        fileModule,
        databaseModule,
        utilModule
    )
}