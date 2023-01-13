package com.bgrem.presentation.welcome

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.fragment.app.testing.FragmentScenario
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.intent.Intents.intended
//import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bgrem.app.R
import com.bgrem.app.di.presentationModule
import com.bgrem.domain.launch.SetFirstLaunchedUseCase
import com.bgrem.presentation.main.about.AboutFragment
import com.bgrem.presentation.main.choose.ChooseMediaFragment
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
//import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class WelcomeFragmentTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var scenarioWelcome: FragmentScenario<WelcomeFragment>
//    private lateinit var viewModel: WelcomeViewModel
//
//    @Before
//    fun setup() {
//        scenarioWelcome = launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioWelcome.moveToState(Lifecycle.State.STARTED)
//        loadKoinModules(presentationModule)
//
//        val setFirstLaunchedUseCase = mock<SetFirstLaunchedUseCase>()
//        viewModel = WelcomeViewModel(setFirstLaunchedUseCase)
//    }

    @After
    fun tearDown() {
        unloadKoinModules(presentationModule)
    }

    @Test
    fun isOnDisplayAboutButton() {
        onView(withId(R.id.welcomeInfoImage)).check(matches(isDisplayed()))
    }

//    @Test
//    fun isOnDisplayClickWelcomeStartButton() {
//        viewModel.onGetStartedClicked()
//        onView(withId(R.id.welcomeStartButton)).check(matches(isDisplayed()))
//    }

    @Test
    fun isOnDisplayWelcomeCollage() {
        onView(withId(R.id.welcomeCollageImage)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayWelcomeTitleText() {
        onView(withId(R.id.welcomeTitleText)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnDisplayWelcomeBottomText() {
        onView(withId(R.id.welcomeCaptionText)).check(matches(isDisplayed()))
    }

    //Not
    @Test
    fun clickAboutButton() {
        onView(withId(R.id.welcomeInfoImage)).perform(click())
    }

    //Not
    @Test
    fun clickWelcomeStartButton() {
        onView(withId(R.id.welcomeStartButton)).perform(click())
    }

    //Not
//    @Test
//    fun openChooseMediaFragment() {
//        onView(withId(R.id.welcomeStartButton)).perform(click())
//        val scenarioChooseMediaFragment: FragmentScenario<ChooseMediaFragment> =
//            launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioChooseMediaFragment.moveToState(Lifecycle.State.STARTED)
//    }
//
//    //Not
//    @Test
//    fun openAboutFragment() {
//        onView(withId(R.id.welcomeInfoImage)).perform(click())
//        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()))
//        val scenarioAboutFragment: FragmentScenario<AboutFragment> =
//            launchFragmentInContainer(themeResId = R.style.Theme_AppTheme)
//        scenarioAboutFragment.moveToState(Lifecycle.State.STARTED)
//    }
//
//    //Not
//    @Test
//    fun validateIntentSentToPackage() {
//        onView(withId(R.id.welcomeCaptionText)).perform(click())
//        intended(
//            allOf(
//                toPackage("com.android.chrome")
//            )
//        )
//    }

}
