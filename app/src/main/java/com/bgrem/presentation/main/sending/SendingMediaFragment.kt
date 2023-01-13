package com.bgrem.presentation.main.sending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentSendingMediaBinding
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.extensions.animateEndsDots
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.getSendingTextRes
import com.bgrem.presentation.common.extensions.loadGifDrawable
import com.bgrem.presentation.main.model.SelectedMediaInfo
import com.bgrem.presentation.main.sending.model.SendingMediaAction
import com.bgrem.presentation.main.sending.model.SendingMediaState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SendingMediaFragment : Fragment(R.layout.fragment_sending_media) {

    private var _binding: FragmentSendingMediaBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SendingMediaViewModel> {
        parametersOf(getExtra(KEY_SELECTED_MEDIA_INFO))
    }
    private val listener by lazy { getParentAsListener<SendingMediaListener>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendingMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        spinnerView.loadGifDrawable(R.drawable.spinner)
        errorView.setOnErrorActionButtonClickListener {
            viewModel.sendMedia()
        }
        sendingMediaText.animateEndsDots(
            maxDotsCount = AppConstants.ANIMATED_DOTS_COUNT,
            durationInMillis = AppConstants.ANIMATED_DOTS_DURATION
        )
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

        selectedMediaType.observe(viewLifecycleOwner) { mediaType ->
            binding.sendingMediaText.setText(mediaType.getSendingTextRes())
        }
    }

    private fun handleAction(action: SendingMediaAction) = when (action) {
        is SendingMediaAction.MediaSent -> listener.onMediaSent(
            task = action.task,
            mediaType = action.mediaType
        )
    }

    private fun applyState(state: SendingMediaState) = with(binding) {
        errorView.isVisible = state.error != null
        loadingGroup.isVisible = state.isLoading
        state.error?.let { errorView.handleError(it) }
    }

    companion object {
        val TAG: String = SendingMediaFragment::class.java.simpleName

        private const val KEY_SELECTED_MEDIA_INFO = "KEY_SELECTED_MEDIA_INFO"

        fun newInstance(selectedMediaInfo: SelectedMediaInfo) = SendingMediaFragment().apply {
            arguments = bundleOf(KEY_SELECTED_MEDIA_INFO to selectedMediaInfo)
        }
    }
}