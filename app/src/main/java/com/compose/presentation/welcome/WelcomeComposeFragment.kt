package com.compose.presentation.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.compose.screen.WelcomeScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeComposeFragment : Fragment() {

    private val welcomeComposeViewModel: WelcomeComposeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            lifecycleScope.launchWhenStarted {
                welcomeComposeViewModel.errorMessage.collect { errorMessage ->
                    setContent {
                        val navController = findNavController()
                        BgRemTheme {
                            WelcomeScreen(
                                navController = navController,
                                welcomeComposeViewModel = welcomeComposeViewModel,
                                errorMessage = errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            welcomeComposeViewModel.linkActionState.collect { action ->
                when (action) {
                    LinkAction.PoliceLink -> { onPolicyClick() }
                    LinkAction.Terms -> { onTermsClick() }
                    else -> {}
                }
            }
        }
    }

    private fun onTermsClick(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://bgrem.deelvin.com/terms_of_use/")
            })
        } catch (_: Exception) {
            showError(resources.getString(
                R.string.common_error_not_found_app_on_device))
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
        welcomeComposeViewModel.onErrorShow(message)
    }
}