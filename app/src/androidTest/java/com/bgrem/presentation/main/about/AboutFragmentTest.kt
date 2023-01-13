package com.bgrem.presentation.main.about

//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers
//import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import com.bgrem.presentation.main.MainActivity
import com.bgrem.presentation.welcome.WelcomeFragment
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutFragmentTest {

//    @get:Rule
//    val intentsTestRule = IntentsTestRule(MainActivity::class.java)
//
//    private lateinit var scenarioAbout: FragmentScenario<AboutFragment>
//
//    @Before
//    fun setup() {
//        scenarioAbout = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioAbout.moveToState(Lifecycle.State.STARTED)
//    }

    @Test
    fun isOnDisplayAboutToolbar() {
        onView(withId(R.id.aboutToolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun textAboutToolbar() {
        onView(withId(R.id.aboutToolbar)).check(matches(hasDescendant(withText(R.string.text_toolbar_test))))
    }

    @Test
    fun clickButtonAboutToolbar() {
        onView(withId(R.id.aboutToolbar)).perform(click())
    }

    @Test
    fun isOnDisplayDescriptionText() {
        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayGifImage() {
        onView(withId(R.id.infoGifImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplaySloganText() {
        onView(withId(R.id.sloganText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayContactEmailText() {
        onView(withId(R.id.contactEmailText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayPrivacyPolicyText() {
        onView(withId(R.id.privacyPolicyText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayVersionText() {
        onView(withId(R.id.versionText)).check(matches(isDisplayed()))
    }

//    @Test
//    fun validateIntentSentToPackage() {
//        onView(withId(R.id.privacyPolicyText)).perform(click())
//        Intents.intended(
//            Matchers.allOf(
//                IntentMatchers.toPackage("com.android.chrome")
//            )
//        )
//    }

}