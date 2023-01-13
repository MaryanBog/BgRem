package com.bgrem.presentation.background.result

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import com.bgrem.presentation.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackgroundResultFragmentTest{

//    @get:Rule
//    val intentsTestRule = IntentsTestRule(MainActivity::class.java)
//
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var scenarioBackgroundResult: FragmentScenario<BackgroundResultFragment>
//
//    @Before
//    fun setup(){
//        scenarioBackgroundResult = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioBackgroundResult.moveToState(Lifecycle.State.STARTED)
//    }

    @Test
    fun isOnDisplayBackArrowImage(){
        onView(withId(R.id.backArrowImage)).check(matches(isDisplayed()))
    }
}