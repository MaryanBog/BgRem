package com.bgrem.presentation.main.sending

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import com.bgrem.app.di.presentationModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.compat.ScopeCompat.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module


@RunWith(AndroidJUnit4::class)
class SendingMediaFragmentTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var scenarioSendingMedia: FragmentScenario<SendingMediaFragment>
//
//    @Before
//    fun setup() {
//        scenarioSendingMedia = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioSendingMedia.moveToState(Lifecycle.State.STARTED)
//        loadKoinModules(presentationModule)
//    }

    @After
    fun tearDown() {
        unloadKoinModules(presentationModule)
    }

    @Test
    fun isDisplayedSendingText() {
        onView(withId(R.id.sendingMediaText)).check(matches(isDisplayed()))
    }

    @Test
    fun isEnabledSpinnerView() {
        onView(withId(R.id.spinnerView)).check(matches(isEnabled()))
    }

    @Test
    fun isEnableErrorView() {
        onView(withId(R.id.errorView)).check(matches(isEnabled()))
    }


}