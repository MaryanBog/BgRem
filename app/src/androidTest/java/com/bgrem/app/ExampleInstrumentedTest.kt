package com.bgrem.app

//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.presentation.welcome.WelcomeFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.bgrem.app", appContext.packageName)
    }

//    private lateinit var scenarioWelcome: FragmentScenario<WelcomeFragment>
//
//    @Before
//    fun setup() {
//        scenarioWelcome = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioWelcome.moveToState(Lifecycle.State.STARTED)
//    }
//
//    @Test
//    fun clickAboutButton() {
//        onView(withId(R.id.welcomeInfoImage)).perform(click())
//    }
//
//    @Test
//    fun clickWelcomeStartButton() {
//        onView(withId(R.id.welcomeStartButton)).perform(click())
//    }

}
