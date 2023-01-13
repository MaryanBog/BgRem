package com.compose.presentation.gallery.image

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
import com.compose.data.models.GetImageTypesParams
import com.compose.screen.GalleryScreen
import com.compose.ui.theme.BgRemTheme
import com.compose.presentation.remove.RemoveViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageGalleryFragment : Fragment() {

    private val viewModel: RemoveViewModel by activityViewModels()

    private val pickImageContract = registerForActivityResult(OpenImageContract()) { uri ->
        uri ?: findNavController().popBackStack()
        handleSelectedImage(uri)
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: findNavController().popBackStack()
            handleSelectedImage(uri)
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
                GetImageTypesParams(viewModel.imageMimeTypes).let {
                    pickImageContract.launch(it)
                }
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                showError(resources.getString(
                    R.string.common_error_no_granted_read_storage_permission))
            }
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
            viewModel.imageActionState.collect{ action ->
                when(action){
                    ImageAction.StartImage -> {

                    }
                    ImageAction.Error -> {
                        showError(resources.getString(
                            R.string.common_error_something_went_wrong))
                    }
                    is ImageAction.ImageSelected -> {
                        viewModel.onMediaSelected(
                            file = action.file,
                            mediaType = action.mediaType,
                            mimeType = action.mimeType,
                            uri = null
                        )

                        viewModel.observeRemovingBackgroundProgress()
                        viewModel.removeBg()
                        findNavController().navigate(
                            R.id.action_imageGalleryFragment_to_loadingComposeFragment
                        )
                    }
                }
            }
        }
    }

    private fun onGalleryClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              pickImage.launch(
              PickVisualMediaRequest(
              ActivityResultContracts.PickVisualMedia.ImageOnly
              ))
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

    private fun handleSelectedImage(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }
            viewModel.onImageSelected(fileStream, mimeType)
        }
    } catch (_: Exception) {
        showError(resources.getString(R.string.common_error_something_went_wrong))
    }

    private fun showError(message: String){
        viewModel.onErrorShow(message)
        findNavController().popBackStack()
    }
}