package com.bgrem.presentation.background.result

import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentBackgroundResultBinding
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.result.model.BackgroundResultAction
import com.bgrem.presentation.background.result.model.HandleResultType
import com.bgrem.presentation.common.dialog.BlockingProgressDialog
import com.bgrem.presentation.common.extensions.dpToPx
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.getResultTypeRes
import com.bgrem.presentation.common.extensions.getSaveResultSuccessMessageRes
import com.bgrem.presentation.common.extensions.launchIntentIfAvailable
import com.bgrem.presentation.common.extensions.loadImageByUrlWithRoundedCorners
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.common.extensions.showSuccessSnack
import com.bgrem.presentation.common.utils.IntentUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class BackgroundResultFragment : Fragment(R.layout.fragment_background_result) {

    private var _binding: FragmentBackgroundResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BackgroundResultViewModel> {
        parametersOf(getExtra(KEY_TASK), getExtra(KEY_MEDIA_TYPE))
    }
    private val listener by lazy { getParentAsListener<BackgroundResultListener>() }
    private val resultPreviewPlayer by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    private val contentUrlString: String get() = viewModel.contentUrl

    private var blockingProgressDialog: BlockingProgressDialog? = null

    private val requestExternalStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.saveMedia()
            }
        }
    private val imageResultCornerRadius by lazy { 16.dpToPx }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackgroundResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onResume() {
        super.onResume()
        if (!resultPreviewPlayer.isPlaying) {
            resultPreviewPlayer.play()
        }
    }

    override fun onPause() {
        super.onPause()
        resultPreviewPlayer.pause()
    }

    private fun initView() = with(binding) {
        yourFileTextView.text = getString(viewModel.mediaType.getResultTypeRes())

        when (viewModel.mediaType) {
            MediaType.VIDEO -> showVideo(viewModel.contentUrl)
            MediaType.IMAGE -> showImage(viewModel.contentUrl)
        }

        resultVideoPlayerView.apply {
            player = resultPreviewPlayer
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    if (view == null || outline == null) return
                    outline.setRoundRect(0, 0, view.width, view.height, 15f.dpToPx)
                }
            }
        }

        resultVideoPlayerView.setOnClickListener {
            listener.onFullScreenClicked(contentUrlString, viewModel.mediaType)
        }

        resultImage.setOnClickListener {
            resultVideoPlayerView.isVisible = false
            listener.onFullScreenClicked(contentUrlString, viewModel.mediaType)
        }

        setClickListeners()
    }

    private fun setClickListeners() = with(binding) {
        backArrowImage.setOnClickListener {
            listener.onResultBackCLicked()
        }

        addFileTextView.setOnClickListener {
            listener.onAddNewFileClicked()
            firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                this.putString(
                    KEY_RESULT, "add_new_file"
                )
            })
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.saveItem -> {
                    onSaveFileClicked()
                    firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                        this.putString(
                            KEY_RESULT, "save"
                        )
                    })
                }
                R.id.shareItem -> {
                    onShareFileClicked()
                    firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                        this.putString(
                            KEY_RESULT, "share"
                        )
                    })
                }
                R.id.shareAppItem -> {
                    onShareAppClicked()
                    firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                        this.putString(
                            KEY_RESULT, "share_app"
                        )
                    })
                }
            }
            true
        }
    }

    private fun observeViewModel() = with(viewModel) {
        event.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }

        isPortraitMedia.observe(viewLifecycleOwner) { isPortrait ->
            binding.resultImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                constrainedWidth = !isPortrait
                constrainedHeight = isPortrait
            }
            binding.resultVideoPlayerView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                constrainedWidth = !isPortrait
                constrainedHeight = isPortrait
            }
        }
    }

    private fun handleAction(action: BackgroundResultAction) = when (action) {
        is BackgroundResultAction.DownloadFile -> BlockingProgressDialog.create()
            .also { blockingProgressDialog = it }
            .show(childFragmentManager, BlockingProgressDialog.TAG)

        is BackgroundResultAction.DownloadSuccessful -> {
            hideLoadingDialog()
            when (viewModel.handleResultType) {
                HandleResultType.SAVE -> saveFile()
                HandleResultType.SHARE -> shareFile(action.file)
            }
        }

        is BackgroundResultAction.SaveFileSucceeded -> {
            showSuccessSnack(viewModel.mediaType.getSaveResultSuccessMessageRes())
        }
        is BackgroundResultAction.Error -> {
            blockingProgressDialog?.dismiss()
            blockingProgressDialog = null
            showErrorSnack(action.messageRes)
        }
    }

    private fun showImage(url: String) = with(binding) {
        resultVideoPlayerView.isVisible = false
        resultImage.loadImageByUrlWithRoundedCorners(url = url, radius = imageResultCornerRadius)
    }

    private fun showVideo(url: String) {
        binding.resultImage.isInvisible = true
        resultPreviewPlayer.apply {
            addMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }

    private fun onShareAppClicked() = launchIntentIfAvailable(
        intent = IntentUtils.getSendTextIntent(
            getString(R.string.play_market, requireActivity().packageName)
        )
    ) {
        showErrorSnack(R.string.common_error_not_found_app_on_device)
    }

    private fun onShareFileClicked() {
        viewModel.onShareResultClick()
    }

    private fun onSaveFileClicked() {
        viewModel.onSaveResultClick()
        viewModel.saveType.observe(viewLifecycleOwner) {
            if (it == 0) {
                firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                    this.putString(
                        KEY_RESULT, "save_video"
                    )
                })

                firebaseAnalytics.logEvent("UseResultMedia_Save_Video_Only_Android", Bundle().apply {
                    this.putString(
                        KEY_RESULT, "save_video_only"
                    )
                })

            } else if (it == 1) {
                firebaseAnalytics.logEvent("UseResultMedia_Android", Bundle().apply {
                    this.putString(
                        KEY_RESULT, "save_photo"
                    )
                })
            }
        }
    }


    private fun shareFile(file: File) = launchIntentIfAvailable(
        intent = IntentUtils.getShareMediaIntent(
            mediaUri = FileProvider.getUriForFile(
                requireContext(),
                getString(R.string.file_provider_authority),
                file
            ),
            mimeType = viewModel.getMimeTypeForResultFile()
        )
    ) {
        showErrorSnack(R.string.common_error_not_found_app_on_device)
    }

    private fun saveFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel.saveMedia()
        } else {
            requestExternalStoragePermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun hideLoadingDialog() {
        blockingProgressDialog?.dismiss()
        blockingProgressDialog = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        resultPreviewPlayer.stop()
    }

    companion object {
        val TAG: String = BackgroundResultFragment::class.java.simpleName

        private const val KEY_TASK = "KEY_TASK"
        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"
        private const val KEY_RESULT = "KEY_RESULT"

        fun newInstance(task: Task, mediaType: MediaType) =
            BackgroundResultFragment().apply {
                arguments = bundleOf(
                    KEY_TASK to task,
                    KEY_MEDIA_TYPE to mediaType
                )
            }
    }
}