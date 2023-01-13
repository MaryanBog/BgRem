package com.bgrem.presentation.main

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.job.ClearJobUseCase
import com.bgrem.domain.launch.GetIsFirstLaunchUseCase
import com.bgrem.getOrAwaitValue
//import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
//import org.mockito.kotlin.mock
import java.io.File

@RunWith(AndroidJUnit4::class)
class MainViewModelTest{
//
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: MainViewModel
//
//    @Before
//    fun setup(){
//        val selectOtherMedia = true
//        val getIsFirstLaunchUseCase = mock<GetIsFirstLaunchUseCase>()
//        val clearJobUseCase = mock<ClearJobUseCase>()
//        viewModel = MainViewModel(selectOtherMedia, getIsFirstLaunchUseCase, clearJobUseCase)
//    }

//    @Test
//    fun onMediaSelectedIsTrue(){
//        val file = File("")
//        val mediaType = MediaType.IMAGE
//        val mimeType = "jpeg"
//        viewModel.onMediaSelected(file, mediaType, mimeType)
//        val value = viewModel.event.getOrAwaitValue().hasBeenHandled
//
//        assertThat(!value).isTrue()
//    }
//
//    @Test
//    fun onGetStartedClickedIsTrue(){
//        viewModel.onGetStartedClicked()
//        val value = viewModel.event.getOrAwaitValue().hasBeenHandled
//
//        assertThat(!value).isTrue()
//    }
//
//    @Test
//    fun onSplashAnimationEndedIsTrue(){
//        viewModel.onSplashAnimationEnded()
//        val value = viewModel.event.getOrAwaitValue().hasBeenHandled
//
//        assertThat(!value).isTrue()
//    }
}