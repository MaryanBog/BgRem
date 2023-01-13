package com.bgrem.presentation.background.removing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentRemoveBackgroundBinding
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.background.removing.model.RemoveBackgroundAction
import com.bgrem.presentation.background.removing.model.RemoveBackgroundState
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.extensions.animateEndsDots
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RemoveBackgroundFragment : Fragment(R.layout.fragment_remove_background) {

    private var _binding: FragmentRemoveBackgroundBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<RemoveBackgroundViewModel> {
        parametersOf(getExtra(KEY_BACKGROUND_ID), getExtra(KEY_MEDIA_TYPE))
    }
    private val listener by lazy { getParentAsListener<RemoveBackgroundListener>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemoveBackgroundBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() = with(binding) {
        errorView.setOnErrorActionButtonClickListener {
            viewModel.removeBackground()
        }
        removingBackgroundText.animateEndsDots(
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
    }

    private fun applyState(state: RemoveBackgroundState) = with(binding) {
        progressText.text = getString(R.string.removing_background_progress, state.progress)
        loadingProgress.progress = state.progress
        errorView.isVisible = state.error != null
        contentGroup.isVisible = state.error == null
        state.error?.let { errorView.handleError(it) }
    }

    private fun handleAction(action: RemoveBackgroundAction) = when (action) {
        is RemoveBackgroundAction.RemovingFinished -> listener.onRemovingFinished(task = action.task)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = RemoveBackgroundFragment::class.java.simpleName

        private const val KEY_BACKGROUND_ID = "KEY_BACKGROUND_ID"
        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"

        fun newInstance(backgroundId: String?, mediaType: MediaType) =
            RemoveBackgroundFragment().apply {
                arguments = bundleOf(KEY_BACKGROUND_ID to backgroundId, KEY_MEDIA_TYPE to mediaType)
            }
    }
}