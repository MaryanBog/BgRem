package com.compose.presentation.finalFragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.bgrem.domain.common.media.MediaType
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.FinalScreen
import com.compose.ui.theme.BgRemTheme
import com.compose.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

@AndroidEntryPoint
class FinalComposeFragment: Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val backgroundViewModel: BackgroundViewModel by activityViewModels()
    private val finalComposeViewModel: FinalComposeViewModel by viewModels()

    private val requestExternalStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                finalComposeViewModel.finalSaveFile()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    removeViewModel.task.observe(viewLifecycleOwner) { task ->
                        removeViewModel.job.observe(viewLifecycleOwner) { job ->
                            setContent {
                                val navController = findNavController()
                                BgRemTheme {
                                    FinalScreen(
                                        navController = navController,
                                        task = task,
                                        removeViewModel = removeViewModel,
                                        backgroundViewModel = backgroundViewModel,
                                        size = counterPrinter(job.size),
                                        finalComposeViewModel = finalComposeViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            finalComposeViewModel.finalResultAction.collect{ action ->
                when(action){
                    FinalResultAction.Error -> {
                        finalComposeViewModel.showProgressBar(false)
                        showError(resources.getString(
                            R.string.common_error_something_went_wrong))
                    }
                    FinalResultAction.Plug -> {
                        showError(resources.getString(
                            R.string.common_error_not_found_app_on_device))
                    }
                    FinalResultAction.Loading -> {
                        finalComposeViewModel.showProgressBar(true)
                    }
                    FinalResultAction.Start -> {
                        finalComposeViewModel.showProgressBar(false)
                    }
                    FinalResultAction.Saved -> {
                        finalComposeViewModel.showProgressBar(false)
                        saveFile()
                        showError(resources.getString(
                            R.string.final_save_file
                        ))
                    }
                    FinalResultAction.SharedApp -> {
                        finalComposeViewModel.showProgressBar(false)
                        try {
                            sharedApp()
                        } catch (_: Exception) {
                            showError(resources.getString(
                                R.string.common_error_not_found_app_on_device))
                        }
                    }
                    is FinalResultAction.SharedFile -> {
                        finalComposeViewModel.showProgressBar(false)
                        try {
                            sharedFile(action.file)
                        } catch (_: Exception) {
                            showError(resources.getString(
                                R.string.common_error_not_found_app_on_device))
                        }
                    }
                    is FinalResultAction.SharedInstagram -> {
                        finalComposeViewModel.showProgressBar(false)
                        try {
                            createInstagramIntent(action.file)
                        } catch (_: Exception) {
                            showError(resources.getString(
                                R.string.common_error_not_found_app_on_device))
                        }
                    }
                }
            }
        }
    }

    private fun sharedApp(){
        val message = getString(R.string.play_market, requireActivity().packageName)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = Constant.PLAIN_TEXT
        }
        intent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun createInstagramIntent(file: File){
        val mimeType = if(finalComposeViewModel.currentMediaType == MediaType.IMAGE){
            resources.getString(R.string.mime_type_image_jpeg)
        } else {
            resources.getString(R.string.mime_type_video_mp4)
        }
        val mediaUri = FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.file_provider_authority),
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, mediaUri)
            setPackage("com.instagram.android")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context?.grantUriPermission(
            "com.instagram.android", mediaUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share to"))
    }

    private fun sharedFile(file: File){
        val mediaUri = FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.file_provider_authority),
            file
        )
        val mimeType = if(finalComposeViewModel.currentMediaType == MediaType.IMAGE){
            resources.getString(R.string.mime_type_image_jpeg)
        } else {
            resources.getString(R.string.mime_type_video_mp4)
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, mediaUri)
            type = mimeType
        }
        intent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun saveFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            finalComposeViewModel.finalSaveFile()
        } else {
            requestExternalStoragePermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun counterPrinter(counterForPrint: Long): String {
        val size = counterForPrint/1000
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val roundoff = df.format(size.toDouble())
        return roundoff.toString()
    }

    private fun showError(message: String) {
        finalComposeViewModel.onErrorShow(message)
    }
}
