package com.bgrem.presentation.main.choose

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentChooseMediaBinding
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.common.contract.GetContentByMimeTypesParams
import com.bgrem.presentation.common.contract.OpenDocumentByMimeTypes
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.main.choose.model.ChooseMediaAction
import com.bgrem.presentation.media.CaptureMediaContract
import com.bgrem.presentation.trimming.TrimmingContract
import com.google.android.material.button.MaterialButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.FileNotFoundException

class ChooseMediaFragment :
    Fragment(R.layout.fragment_choose_media),
    SelectMediaChooserBottomSheet.SelectMediaChooserListener {

    private var _binding: FragmentChooseMediaBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChooseMediaViewModel>()
    private val listener by lazy { getParentAsListener<ChooseMediaListener>() }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var checkedButton: MaterialButton? = null
    private val pickMediaContract = registerForActivityResult(OpenDocumentByMimeTypes()) { uri ->
        uri ?: return@registerForActivityResult
        when (viewModel.selectedMediaType) {
            MediaType.IMAGE -> handleSelectedMedia(uri)
            MediaType.VIDEO -> viewModel.onTakeVideoSucceeded(uri)
            else -> Unit
        }
    }
    private val captureMedia = registerForActivityResult(CaptureMediaContract()) { uri ->
        uri ?: return@registerForActivityResult
        when (viewModel.selectedMediaType) {
            MediaType.IMAGE -> {
                viewModel.onTakePhotoSucceeded(uri)
                firebaseAnalytics.logEvent(
                    "Choose_file_android",
                    Bundle().apply { this.putString(KEY_FILE_CHOOSE, "photo_chosen") })
            }
            MediaType.VIDEO -> {
                viewModel.onTakeVideoSucceeded(uri)
                firebaseAnalytics.logEvent(
                    "Choose_file_android",
                    Bundle().apply { this.putString(KEY_FILE_CHOOSE, "video_chosen") })
            }
            else -> Unit
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            when (viewModel.selectedMediaType) {
                MediaType.IMAGE -> handleSelectedMedia(uri)
                MediaType.VIDEO -> viewModel.onTakeVideoSucceeded(uri)
                else -> Unit
            }
        }

    private val trimming = registerForActivityResult(TrimmingContract()) { uri ->
        uri ?: return@registerForActivityResult
        handleSelectedMedia(uri)
    }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.selectedMediaType?.let {
                    GetContentByMimeTypesParams(
                        type = it,
                        mimeTypes = it.toMimeTypes()
                    )
                }?.let { pickMediaContract.launch(it) }
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                showErrorSnack(R.string.common_error_no_granted_read_storage_permission)
            }
        }

    private val requestWritePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                checkPermissionWriteExternalStorage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        initView()
        observeViewModel()
    }

    private fun initView() = with(binding) {
        infoImage.setOnClickListener {
            listener.onAboutAppClicked()
        }

        videoButton.setOnClickListener {
            viewModel.onSelectMediaType(MediaType.VIDEO)
        }

        imageButton.setOnClickListener {
            viewModel.onSelectMediaType(MediaType.IMAGE)
        }
    }

    private fun observeViewModel() = with(viewModel) {
        event.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }
    }

    private fun handleAction(action: ChooseMediaAction) = when (action) {
        ChooseMediaAction.ClearSelectionMediaType -> checkedButton?.toggle()
        ChooseMediaAction.SelectImage -> {
            checkedButton = binding.imageButton
            showMediaChooserBottomSheet(MediaType.IMAGE)
        }
        ChooseMediaAction.SelectVideo -> {
            checkedButton = binding.videoButton
            showMediaChooserBottomSheet(MediaType.VIDEO)
        }
        is ChooseMediaAction.Error -> showErrorSnack(action.errorMessageRes)
        is ChooseMediaAction.MediaSelected -> listener.onMediaSelected(
            file = action.file,
            mediaType = action.mediaType,
            mimeType = action.mimeType,
            uri = action.uri
        )
        is ChooseMediaAction.ThumbnailSelected -> {
            trimming.launch(action.uri)
        }
    }

    private fun showMediaChooserBottomSheet(mediaType: MediaType) {
        SelectMediaChooserBottomSheet.create(mediaType)
            .show(childFragmentManager, SelectMediaChooserBottomSheet.TAG)
    }

    private fun handleSelectedMedia(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }
            viewModel.onMediaSelected(fileStream, mimeType, uri)
        }
    } catch (e: Exception) {
        Timber.e(e)
        showErrorSnack(R.string.common_error_something_went_wrong)
    } catch (e: FileNotFoundException) {
        Timber.e(e)
        showErrorSnack(R.string.common_error_file_not_found)
    }

    private fun launchTakePhoto() {
        captureMedia.launch(MediaType.IMAGE)
    }

    private fun launchTakeVideo() {
        captureMedia.launch(MediaType.VIDEO)
    }

    override fun onCameraClick() = when (viewModel.selectedMediaType) {
        MediaType.IMAGE -> launchTakePhoto()
        MediaType.VIDEO -> launchTakeVideo()
        else -> Unit
    }


    override fun onGalleryClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (viewModel.selectedMediaType) {
                MediaType.IMAGE -> {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
                MediaType.VIDEO -> {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                }
                else -> {
                    return
                }
            }
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

    override fun onDismiss() {
        checkedButton?.toggle()
        checkedButton = null
    }

    private fun MediaType.toMimeTypes() = when (this) {
        MediaType.VIDEO -> viewModel.videoMimeTypes
        MediaType.IMAGE -> viewModel.imageMimeTypes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_FILE_CHOOSE = "KEY_FILE_CHOOSE"
        val TAG: String = ChooseMediaFragment::class.java.simpleName

        fun newInstance() = ChooseMediaFragment()
    }
}
