package com.bgrem.presentation.media.capture

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.di.presentationModule
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.getOrAwaitValue
import com.bgrem.presentation.media.capture.model.CaptureAction
//import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
//import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class CaptureViewModelTest{

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: CaptureViewModel
//
//    @Before
//    fun setup(){
//        val fileStorage = mock<FileStorage>()
//        val mimeTypeManager = mock<MimeTypeManager>()
//        val mediaType = MediaType.IMAGE
//        viewModel = CaptureViewModel(fileStorage, mimeTypeManager, mediaType)
//        loadKoinModules(presentationModule)
//    }

    @After
    fun tearDown() {
        unloadKoinModules(presentationModule)
    }

    //Not
//    @Test
//    fun onStartVideoRecordingIsTrue(){
//        viewModel.onStartVideoRecording()
//        val value = viewModel.event.getOrAwaitValue().getContentIfNotHandled()
//
//        Truth.assertThat(value).isEqualTo(CaptureAction.OnVideoRecordingStopped)
//    }
}