package com.compose.presentation.gallery.video

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.bgrem.presentation.trimming.TrimmingContract
import com.compose.data.models.GetImageTypesParams
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.GalleryScreen
import com.compose.ui.theme.BgRemTheme

class VideoGalleryFragment : Fragment() {

    private val viewModel: RemoveViewModel by activityViewModels()

    private val pickVideoContract = registerForActivityResult(OpenVideoContract()) { uri ->
        uri ?: findNavController().popBackStack()
        viewModel.onTakeVideo(uri)
    }

    private val pickVideo =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: findNavController().popBackStack()
            viewModel.onTakeVideo(uri)
        }

    private val requestWritePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                checkPermissionWriteExternalStorage()
            }
        }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                GetImageTypesParams(viewModel.videoMimeTypes).let {
                    pickVideoContract.launch(it)
                }
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                showError(
                    resources.getString(
                        R.string.common_error_no_granted_read_storage_permission
                    )
                )
            }
        }

    private val trimming = registerForActivityResult(TrimmingContract()) { uri ->
        uri ?: findNavController().popBackStack()
        handleSelectedVideo(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BgRemTheme {
                    GalleryScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onGalleryClick()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.videoActionState.collect { action ->
                when (action) {
                    VideoAction.StartVideo -> {}
                    VideoAction.Error -> {
                        showError(
                            resources.getString(
                                R.string.common_error_something_went_wrong
                            )
                        )
                    }
                    is VideoAction.VideoSelected -> {
                        viewModel.onMediaSelected(
                            file = action.file,
                            mediaType = action.mediaType,
                            mimeType = action.mimeType,
                            uri = action.uri
                        )

                        viewModel.observeRemovingBackgroundProgress()
                        viewModel.removeBg()
                        findNavController().navigate(
                            R.id.action_videoGalleryFragment_to_loadingComposeFragment
                        )

                    }
                    is VideoAction.TruncateVideo -> {
                        trimming.launch(action.uri)
                    }
                }
            }
        }
    }

    private fun onGalleryClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickVideo.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.VideoOnly
                )
            )
        } else {
            requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun checkPermissionWriteExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun handleSelectedVideo(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }
            viewModel.onVideoSelected(fileStream, mimeType, uri)
        }
    } catch (_: Exception) {
        showError(resources.getString(R.string.common_error_something_went_wrong))
    }

    private fun showError(message: String) {
        viewModel.onErrorShow(message)
        findNavController().popBackStack()
    }
}