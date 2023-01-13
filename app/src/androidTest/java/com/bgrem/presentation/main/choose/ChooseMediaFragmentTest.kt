package com.bgrem.presentation.main.choose

//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChooseMediaFragmentTest {

//    private lateinit var scenarioChooseMedia: FragmentScenario<ChooseMediaFragment>
//
//    @Before
//    fun setup() {
//        scenarioChooseMedia = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioChooseMedia.moveToState(Lifecycle.State.STARTED)
//    }

    @Test
    fun isOnDisplayInfoButton() {
        onView(withId(R.id.infoImage)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayTitle() {
        onView(withId(R.id.titleText)).check(matches(isDisplayed()))
    }

    @Test
    fun isTitleTextMatches() {
        onView(withId(R.id.titleText)).check(matches(withText(R.string.title_choose_fragment_test)))
    }

    @Test
    fun isOnDisplayVideoButton() {
        onView(withId(R.id.videoButton)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayImageButton() {
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickVideoButton() {
        onView(withId(R.id.videoButton)).perform(click())
    }

    @Test
    fun clickImageButton() {
        onView(withId(R.id.imageButton)).perform(click())
    }

//    @Test
//    fun performVideoChooseFragment() {
//        onView(withId(R.id.videoButton)).perform(click())
//        val scenarioBottomSheet: FragmentScenario<SelectMediaChooserBottomSheet> =
//            launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioBottomSheet.moveToState(Lifecycle.State.STARTED)
//    }
//
//    @Test
//    fun performImageChooseFragment() {
//        onView(withId(R.id.imageButton)).perform(click())
//        val scenarioBottomSheet: FragmentScenario<SelectMediaChooserBottomSheet> =
//            launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioBottomSheet.moveToState(Lifecycle.State.STARTED)
//    }


}