package com.compose.presentation.background

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.compose.data.models.BackgroundType
import com.compose.screen.BackgroundsImageScreen
import com.compose.screen.BackgroundsScreen
import com.compose.ui.theme.BgRemTheme

class BackgroundsComposeFragment : Fragment() {

    private val backgroundsViewModel: BackgroundViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())

        backgroundsViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                val toast = Toast.makeText(
                    context,
                    R.string.common_error_something_went_wrong,
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
            }
        }

        if (backgroundsViewModel.backgroundType == BackgroundType.IMAGE) {
            backgroundsViewModel.imageFromCategory.observe(viewLifecycleOwner) {
                view.setContent {
                    val navController = findNavController()
                    BgRemTheme {
                        BackgroundsImageScreen(navController, backgroundsViewModel, it)
                    }
                }
            }

        } else if (backgroundsViewModel.backgroundType == BackgroundType.VIDEO) {
            backgroundsViewModel.videoFromCategory.observe(viewLifecycleOwner) {
                view.setContent {
                    val navController = findNavController()
                    BgRemTheme {
                        BackgroundsScreen(navController, backgroundsViewModel, it)
                    }
                }
            }
        }

        return view
    }

}