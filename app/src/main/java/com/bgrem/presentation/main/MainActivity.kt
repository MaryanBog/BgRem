package com.bgrem.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bgrem.app.R
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.ChangeBackgroundActivity
import com.bgrem.presentation.common.extensions.disableKeepScreenOn
import com.bgrem.presentation.common.extensions.doThenFinish
import com.bgrem.presentation.common.extensions.enableKeepScreenOn
import com.bgrem.presentation.common.extensions.getCurrentFragment
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.replaceFragment
import com.bgrem.presentation.main.about.AboutClickListener
import com.bgrem.presentation.main.about.AboutFragment
import com.bgrem.presentation.main.choose.ChooseMediaFragment
import com.bgrem.presentation.main.choose.ChooseMediaListener
import com.bgrem.presentation.main.model.MainAction
import com.bgrem.presentation.main.model.SelectedMediaInfo
import com.bgrem.presentation.main.sending.SendingMediaFragment
import com.bgrem.presentation.main.sending.SendingMediaListener
import com.bgrem.presentation.main.splash.SplashFragment
import com.bgrem.presentation.main.splash.SplashListener
import com.bgrem.presentation.welcome.ChangeFragmentListener
import com.bgrem.presentation.welcome.WelcomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class MainActivity :
    AppCompatActivity(R.layout.activity_main),
    ChooseMediaListener,
    SplashListener,
    SendingMediaListener,
    ChangeFragmentListener,
    AboutClickListener{

    private val viewModel by viewModel<MainViewModel> {
        parametersOf(getExtra(KEY_SELECT_OTHER_MEDIA, false))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment() ?: return
        when (currentFragment) {
            is WelcomeFragment,
            is SplashFragment,
            is ChooseMediaFragment -> finish()

            is SendingMediaFragment -> {
                showChooseMediaFragment()
                disableKeepScreenOn()
            }

            is AboutFragment -> super.onBackPressed()
        }
    }

    private fun observeViewModel() = with(viewModel) {
        event.observe(this@MainActivity) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }
    }

    private fun handleAction(action: MainAction) = when (action) {
        is MainAction.SendMedia -> {
            showSendingFragment(action.info)
            enableKeepScreenOn()
        }
        is MainAction.ShowSplash -> showSplashFragment()
        is MainAction.ShowChooseMedia -> showChooseMediaFragment()
        is MainAction.ShowWelcome -> showWelcomeFragment()
    }

    private fun showSendingFragment(selectedMediaInfo: SelectedMediaInfo) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = SendingMediaFragment.TAG
    ) { SendingMediaFragment.newInstance(selectedMediaInfo) }

    private fun showSplashFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = SplashFragment.TAG
    ) { SplashFragment.newInstance() }

    private fun showChooseMediaFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = ChooseMediaFragment.TAG
    ) { ChooseMediaFragment.newInstance() }

    private fun showWelcomeFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = WelcomeFragment.TAG
    ) { WelcomeFragment.newInstance() }

    private fun showAboutFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = AboutFragment.TAG,
        addToBackStack = true
    ) { AboutFragment.newInstance() }

    override fun onMediaSelected(file: File, mediaType: MediaType, mimeType: String, uri: Uri?) {
        viewModel.onMediaSelected(file, mediaType, mimeType, uri)
    }

    override fun onGetInfoClicked() {
        showAboutFragment()
    }

    override fun onSplashAnimationEnded() {
        viewModel.onSplashAnimationEnded()
    }

    override fun onMediaSent(task: Task, mediaType: MediaType) = doThenFinish {
        startActivity(
            ChangeBackgroundActivity.newIntent(
                context = this,
                taskId = task.id,
                mediaType = mediaType
            )
        )
    }

    override fun onGetStartClicked() {
        viewModel.onGetStartedClicked()
    }

    override fun onAboutAppClicked() {
        showAboutFragment()
    }

    override fun onBackClicked() {
        supportFragmentManager.popBackStack()
    }

    companion object {
        private const val KEY_SELECT_OTHER_MEDIA = "KEY_SELECT_OTHER_MEDIA"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
            .putExtra(KEY_SELECT_OTHER_MEDIA, true)
    }
}