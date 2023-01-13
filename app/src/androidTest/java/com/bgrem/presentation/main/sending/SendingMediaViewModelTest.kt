package com.bgrem.presentation.main.sending

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.MainCoroutineRule
import com.bgrem.app.di.presentationModule
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.job.CreateJobAndRemoveBackgroundUseCase
import com.bgrem.getOrAwaitValue
import com.bgrem.presentation.main.model.SelectedMediaInfo
//import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
//import org.mockito.kotlin.mock
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SendingMediaViewModelTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SendingMediaViewModel

//    @Before
//    fun setup() {
//        val file = File("")
//        val mediaType = MediaType.IMAGE
//        val mimeType = "jpeg"
//        val selectedMediaInfo = SelectedMediaInfo(file, mediaType, mimeType)
//        val createJobAndRemoveBackgroundUseCase = mock<CreateJobAndRemoveBackgroundUseCase>()
//        viewModel = SendingMediaViewModel(selectedMediaInfo, createJobAndRemoveBackgroundUseCase)
//        loadKoinModules(presentationModule)
//    }
//
//    @Test
//    fun sendMedia() {
//        viewModel.sendMedia()
//        val value = viewModel.event.getOrAwaitValue().hasBeenHandled
//
//        Truth.assertThat(!value).isTrue()
//    }
}