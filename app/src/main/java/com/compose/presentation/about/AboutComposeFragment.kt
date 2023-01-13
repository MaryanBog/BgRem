package com.compose.presentation.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.compose.presentation.welcome.LinkAction
import com.compose.screen.AboutUsScreen
import com.compose.ui.theme.BgRemTheme

class AboutComposeFragment: Fragment() {

    private val aboutComposeViewModel: AboutComposeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                BgRemTheme {
                    AboutUsScreen(
                        navController = navController,
                        aboutComposeViewModel = aboutComposeViewModel
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            aboutComposeViewModel.linkActionState.collect { action ->
                when (action) {
                    LinkAction.PoliceLink -> { onPolicyClick() }
                    LinkAction.EmailLink -> { onEmailClick() }
                    LinkAction.Facebook -> { onFacebookClick() }
                    LinkAction.Instagram -> { onInstagramClick() }
                    LinkAction.Youtube -> { onYoutubeClick() }
                    else -> {}
                }
            }
        }
    }

    private fun onYoutubeClick(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.youtube.com/")
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_not_found_app_on_device))
        }
    }

    private fun onInstagramClick(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.instagram.com/")
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_not_found_app_on_device))
        }
    }

    private fun onFacebookClick(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.facebook.com/")
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_not_found_app_on_device))
        }
    }

    private fun onEmailClick(){
        try {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(
                    Intent.EXTRA_EMAIL,
                    listOf("support@deelvin.com").toTypedArray()
                )
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_email_client_not_found))
        }
    }

    private fun onPolicyClick(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://bgrem.deelvin.com/privacy_policy/")
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_not_found_app_on_device))
        }
    }

    private fun showError(message: String) {
        aboutComposeViewModel.onErrorShow(message)
    }
}