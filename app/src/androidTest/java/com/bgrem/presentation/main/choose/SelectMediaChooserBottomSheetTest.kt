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
class SelectMediaChooserBottomSheetTest {

//    private lateinit var scenarioBottomSheet: FragmentScenario<SelectMediaChooserBottomSheet>
//
//    @Before
//    fun setup() {
//        scenarioBottomSheet = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioBottomSheet.moveToState(Lifecycle.State.STARTED)
//    }

    @Test
    fun isEnableNoteText() {
        onView(withId(R.id.noteText)).check(matches(isEnabled()))
    }

    @Test
    fun isRightTextDisplay() {
        onView(withId(R.id.noteText)).check(matches(withText(R.string.choose_media_note_test)))
    }

    @Test
    fun isOnDisplayCameraButton() {
        onView(withId(R.id.cameraText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayGalleryButton() {
        onView(withId(R.id.galleryText)).check(matches(isDisplayed()))
    }

    @Test
    fun textGalleryButton() {
        onView(withId(R.id.galleryText)).check(matches(withText(R.string.choose_media_gallery_test)))
    }

    @Test
    fun textCameraButton() {
        onView(withId(R.id.cameraText)).check(matches(withText(R.string.choose_media_camera_test)))
    }

    @Test
    fun isOnDisplayCancelButton() {
        onView(withId(R.id.cancelButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickCancelButton() {
        onView(withId(R.id.cancelButton)).perform(click())
    }

    @Test
    fun textCancelButton() {
        onView(withId(R.id.cancelButton)).check(matches(withText(R.string.common_cancel_test)))
    }

}