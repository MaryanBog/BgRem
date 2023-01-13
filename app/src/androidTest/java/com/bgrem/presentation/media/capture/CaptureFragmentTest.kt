package com.bgrem.presentation.media.capture

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import com.bgrem.app.di.presentationModule
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.launch.SetFirstLaunchedUseCase
import com.bgrem.presentation.welcome.WelcomeViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
//import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class CaptureFragmentTest{

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var scenarioCaptureFragment: FragmentScenario<CaptureFragment>
//    private lateinit var viewModel: CaptureViewModel
//
//    @Before
//    fun setup() {
//        scenarioCaptureFragment = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioCaptureFragment.moveToState(Lifecycle.State.STARTED)
//        loadKoinModules(presentationModule)
//
//        val fileStorage = mock<FileStorage>()
//        val mimeTypeManager = mock<MimeTypeManager>()
//        val mediaType = MediaType.IMAGE
//
//        viewModel = CaptureViewModel(fileStorage, mimeTypeManager, mediaType)
//    }

    @After
    fun tearDown() {
        unloadKoinModules(presentationModule)
    }

    @Test
    fun isOnDisplayCameraSwitchImage(){
        onView(withId(R.id.cameraSwitchImage)).check(matches(isDisplayed()))
    }
}