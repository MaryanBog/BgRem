package com.bgrem.presentation.background.removing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.localstorage.LocalDataStorage
import com.bgrem.domain.task.RemoveBackgroundInteractor
import com.bgrem.domain.task.RemoveBackgroundInteractorImpl
import com.bgrem.domain.task.TaskDataSource
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
//import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class RemoveBackgroundViewModelTest: TestCase(){

    private lateinit var viewModel: RemoveBackgroundViewModel
    private lateinit var removeBackgroundInteractor: RemoveBackgroundInteractorImpl

//    val localDataStorage = mock<LocalDataStorage>()
//    val taskDataSource = mock<TaskDataSource>()
    val backgroundId = "id"
    val mediaType = MediaType.IMAGE

    @Before
    public override fun setUp() {
        super.setUp()


        viewModel = RemoveBackgroundViewModel(
            backgroundId, mediaType, removeBackgroundInteractor
        )
    }

//    @Test
//    fun remove() = runBlocking{
//        removeBackgroundInteractor = RemoveBackgroundInteractorImpl(localDataStorage, taskDataSource)
//
//        viewModel.removeBackground()
//        removeBackgroundInteractor.removeBackground(backgroundId, mediaType)
//    }
}