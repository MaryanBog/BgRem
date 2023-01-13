package com.bgrem.presentation.background.select

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentSelectBackgroundBinding
import com.bgrem.domain.background.model.Background
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.background.contract.CurrentTaskContract
import com.bgrem.presentation.background.select.adapter.BackgroundAdapter
import com.bgrem.presentation.background.select.adapter.BackgroundGroupAdapter
import com.bgrem.presentation.background.select.model.SelectBackgroundAction
import com.bgrem.presentation.background.select.model.SelectBackgroundListener
import com.bgrem.presentation.background.select.model.SelectBackgroundState
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.contract.GetContentByMimeTypesParams
import com.bgrem.presentation.common.contract.OpenDocumentByMimeTypes
import com.bgrem.presentation.common.dialog.BlockingProgressDialog
import com.bgrem.presentation.common.extensions.dpToPx
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.getTitleRes
import com.bgrem.presentation.common.extensions.loadImageByUrl
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.io.FileNotFoundException

class SelectBackgroundFragment :
    Fragment(R.layout.fragment_select_background),
    BackgroundSelectorBottomSheet.BackgroundSelectorListener {

    private var _binding: FragmentSelectBackgroundBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SelectBackgroundViewModel> {
        parametersOf(getExtra(KEY_CONTENT_MEDIA_TYPE))
    }
    private val listener by lazy { getParentAsListener<SelectBackgroundListener>() }
    private val currentTaskContract by lazy { getParentAsListener<CurrentTaskContract>() }
    private var blockingProgressDialog: BlockingProgressDialog? = null
    private val backgroundPreviewPlayer by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val takeMediaContract = registerForActivityResult(OpenDocumentByMimeTypes()) { uri ->
        uri?.let { handleSelectedMedia(it) }
    }
    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val params = GetContentByMimeTypesParams(
                    type = viewModel.selectedBackgroundMediaType,
                    mimeTypes = viewModel.selectedBackgroundMediaType.toMimeTypes()
                )
                takeMediaContract.launch(params)
            } else {
                showErrorSnack(R.string.common_error_no_granted_read_storage_permission)
            }
        }

    private val backgroundAdapterListener = object : BackgroundAdapter.BackgroundAdapterListener {
        override fun onAddNewBgClick() {
            selectUserBackground()
        }

        override fun onSelectedBackground(id: String, group: BackgroundGroup) {
            viewModel.onBackgroundSelected(id, group)
        }
    }
    private val backgroundGroupAdapter = BackgroundGroupAdapter(backgroundAdapterListener)

    private val backgroundPagerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.onGroupSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBackgroundBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeContract()
        observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getIsPortraitMedia()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.observeBackgrounds()
        }
    }

    private fun initView() = with(binding) {
        backImage.setOnClickListener {
            listener.onSelectBackPressed()
        }
        errorView.setOnErrorActionButtonClickListener {
            viewModel.observeBackgrounds()
        }
        bgTypePager.apply {
            isUserInputEnabled = false
            adapter = backgroundGroupAdapter
            registerOnPageChangeCallback(backgroundPagerPageChangeCallback)
        }
        continueButton.setOnClickListener {
            viewModel.onContinue()
            viewModel.backgroundType.observe(viewLifecycleOwner) {
                if (it.equals("transparent")) {
                    firebaseAnalytics.logEvent("ProcessingResultsReceived_Android", Bundle().apply {
                        this.putString(
                            KEY_SELECT, "transparent_background"
                        )
                    })

                }
            }
            viewModel.backgroundId.observe(viewLifecycleOwner) { id ->
                firebaseAnalytics.logEvent("Backgrounds_Android", Bundle().apply {
                    this.putString(
                        KEY_SELECT, "background_id $id"
                    )
                })
                viewModel.backgroundGroup.observe(viewLifecycleOwner) { group ->
                    when (group) {
                        BackgroundGroup.COLOR -> {
                            firebaseAnalytics.logEvent(
                                "ProcessingResultsReceived_Android",
                                Bundle().apply {
                                    this.putString(
                                        KEY_SELECT, "color_background"
                                    )
                                })
                        }
                        BackgroundGroup.IMAGE -> {
                            firebaseAnalytics.logEvent(
                                "ProcessingResultsReceived_Android",
                                Bundle().apply {
                                    this.putString(
                                        KEY_SELECT, "photo_background"
                                    )
                                })
                        }
                        BackgroundGroup.VIDEO -> {
                            firebaseAnalytics.logEvent(
                                "ProcessingResultsReceived_Android",
                                Bundle().apply {
                                    this.putString(
                                        KEY_SELECT, "video_background"
                                    )
                                })
                        }
                        BackgroundGroup.USER -> {
                            firebaseAnalytics.logEvent(
                                "ProcessingResultsReceived_Android",
                                Bundle().apply {
                                    this.putString(
                                        KEY_SELECT, "user_photo_video_background"
                                    )
                                })
                        }

                        else -> {}
                    }

                }
            }
        }

        bgPreviewPlayerView.apply {
            player = backgroundPreviewPlayer
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    if (view == null || outline == null) return
                    outline.setRoundRect(0, 0, view.width, view.height, 15f.dpToPx)
                }
            }
        }

        TabLayoutMediator(bgTypeTabLayout, bgTypePager) { tab, position ->
            tab.text =
                getString(viewModel.availableBackgroundGroups[position].getTitleRes()).uppercase()
        }.attach()
    }

    private fun observeContract() = with(binding) {
        currentTaskContract.taskLiveData.observe(viewLifecycleOwner) { task ->
            previewImage.loadImageByUrl(task.resultUrl.orEmpty())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        state.observe(viewLifecycleOwner) { state ->
            applyState(state)
        }
        event.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }
        backgrounds.observe(viewLifecycleOwner) { data ->
            backgroundGroupAdapter.submitData(data.map { it.second })
        }
        selectedGroupPosition.observe(viewLifecycleOwner) { groupPosition ->
            binding.bgTypePager.currentItem = groupPosition
        }
        isPortraitMedia.observe(viewLifecycleOwner) { isPortrait ->
            binding.previewImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                constrainedWidth = !isPortrait
                constrainedHeight = isPortrait
            }
        }
    }

    private fun applyState(state: SelectBackgroundState) = with(binding) {
        loadingProgress.isVisible = state.isLoading
        errorView.isVisible = state.error != null
        contentGroup.isVisible = state.error == null && !state.isLoading
        bgPreviewPlayerView.isVisible = state.isPreviewPlayerVisible
        state.error?.let { errorView.handleError(it) }
    }

    private fun handleAction(action: SelectBackgroundAction) = when (action) {
        is SelectBackgroundAction.Error -> {
            blockingProgressDialog?.dismiss()
            blockingProgressDialog = null
            showErrorSnack(action.messageRes)
        }
        is SelectBackgroundAction.CreatingBackground -> BlockingProgressDialog.create()
            .also { blockingProgressDialog = it }
            .show(childFragmentManager, BlockingProgressDialog.TAG)
        is SelectBackgroundAction.BackgroundCreated -> {
            blockingProgressDialog?.dismiss()
            blockingProgressDialog = null
        }
        is SelectBackgroundAction.BackgroundSelected -> showBackgroundPreview(action.background)
        is SelectBackgroundAction.Continue -> listener.onBackgroundSelected(action.backgroundId)
        is SelectBackgroundAction.ContinueTransparentImage -> listener.onTransparentSelected()
    }

    private fun selectUserBackground() {
        if (viewModel.contentMediaType == MediaType.VIDEO) {
            BackgroundSelectorBottomSheet.newInstance()
                .show(childFragmentManager, BackgroundSelectorBottomSheet.TAG)
        } else {
            onPhotoClick()
            firebaseAnalytics.logEvent("Upload_Photo_Android", Bundle().apply {
                this.putString(
                    KEY_SELECT, "upload_user_photo_as_background"
                )
            })
        }
    }

    private fun handleSelectedMedia(uri: Uri) = try {
        with(requireContext()) {
            val fileStream = contentResolver.openInputStream(uri)
            val mimeType = contentResolver.getType(uri)
            val fileDescriptor = contentResolver.openFileDescriptor(
                uri,
                AppConstants.FILE_DESCRIPTOR_MODE_READ
            )?.fileDescriptor
            val orientation = fileDescriptor?.let {
                ExifInterface(it).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            }
            viewModel.onMediaSelected(fileStream, mimeType, fileDescriptor, orientation)
        }
    } catch (e: FileNotFoundException) {
        Timber.e(e)
        showErrorSnack(R.string.common_error_file_not_found)
    }


    private fun showBackgroundPreview(background: Background) = with(binding) {
        previewBgImage.backgroundTintList = null
        previewBgImage.setImageDrawable(null)
        resetVideoBackgroundPreviewPlayer()

        when (background.group) {
            BackgroundGroup.IMAGE -> previewBgImage.loadImageByUrl(background.thumbnailUrl.orEmpty())
            BackgroundGroup.COLOR -> previewBgImage.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(background.color.orEmpty()))
            BackgroundGroup.TRANSPARENT -> previewBgImage.setImageResource(R.drawable.ic_transparent)
            BackgroundGroup.VIDEO -> playVideoBackgroundPreview(background.thumbnailUrl.orEmpty())
            BackgroundGroup.USER -> if (background.posterUrl.isNullOrEmpty()) {
                previewBgImage.loadImageByUrl(background.thumbnailUrl.orEmpty())
            } else {
                playVideoBackgroundPreview(background.fileUrl.orEmpty())
            }
        }
    }

    private fun playVideoBackgroundPreview(url: String) = with(backgroundPreviewPlayer) {
        resetVideoBackgroundPreviewPlayer()
        addMediaItem(MediaItem.fromUri(url))
        prepare()
    }

    private fun resetVideoBackgroundPreviewPlayer() = with(backgroundPreviewPlayer) {
        stop()
        clearMediaItems()
    }

    override fun onPhotoClick() {
        viewModel.onSelectBackgroundMediaType(MediaType.IMAGE)
        requestStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onVideoClick() {
        viewModel.onSelectBackgroundMediaType(MediaType.VIDEO)
        requestStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun MediaType.toMimeTypes() = when (this) {
        MediaType.VIDEO -> viewModel.mimeTypeManager.getAvailableVideoMimeTypes()
        MediaType.IMAGE -> viewModel.mimeTypeManager.getAvailableImageMimeTypes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        backgroundPreviewPlayer.stop()
    }

    companion object {
        val TAG: String = SelectBackgroundFragment::class.java.simpleName

        private const val KEY_CONTENT_MEDIA_TYPE = "KEY_CONTENT_MEDIA_TYPE"
        private const val KEY_SELECT = "KEY_SELECT"

        fun newInstance(contentMediaType: MediaType) = SelectBackgroundFragment().apply {
            arguments = bundleOf(KEY_CONTENT_MEDIA_TYPE to contentMediaType)
        }
    }
}