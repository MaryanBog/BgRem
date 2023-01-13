package com.bgrem.presentation.main.splash

//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {

//    private lateinit var scenarioSplash: FragmentScenario<SplashFragment>
//
//    @Before
//    fun setup() {
//        scenarioSplash = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioSplash.moveToState(Lifecycle.State.STARTED)
//    }

    @Test
    fun isOnDisplaySplashGif() {
        onView(withId(R.id.splashGifImage)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplaySplashTitleText() {
        onView(withId(R.id.splashTitleText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplaySplashSubTitleText() {
        onView(withId(R.id.splashSubtitleText)).check(matches(isDisplayed()))
    }

//    @Test
//    fun testFragmentToAnotherState() {
//        scenarioSplash.moveToState(Lifecycle.State.RESUMED)
//    }

}