package com.bgrem.presentation.background

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import com.bgrem.app.R
import com.bgrem.app.databinding.ActivityChangeBackgroundBinding
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.contract.CurrentTaskContract
import com.bgrem.presentation.background.model.ChangeBackgroundAction
import com.bgrem.presentation.background.model.ChangeBackgroundState
import com.bgrem.presentation.background.removing.RemoveBackgroundFragment
import com.bgrem.presentation.background.removing.RemoveBackgroundListener
import com.bgrem.presentation.background.result.BackgroundResultFragment
import com.bgrem.presentation.background.result.BackgroundResultListener
import com.bgrem.presentation.background.select.SelectBackgroundFragment
import com.bgrem.presentation.background.select.model.SelectBackgroundListener
import com.bgrem.presentation.common.extensions.doThenFinish
import com.bgrem.presentation.common.extensions.enableKeepScreenOn
import com.bgrem.presentation.common.extensions.getCurrentFragment
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.replaceFragment
import com.bgrem.presentation.fullScreen.FullScreenActivity
import com.bgrem.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChangeBackgroundActivity :
    AppCompatActivity(R.layout.activity_change_background),
    CurrentTaskContract,
    SelectBackgroundListener,
    RemoveBackgroundListener,
    BackgroundResultListener {

    private var _binding: ActivityChangeBackgroundBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChangeBackgroundViewModel> {
        parametersOf(getExtra(KEY_TASK_ID), getExtra(KEY_CONTENT_MEDIA_TYPE))
    }

    override val taskLiveData: LiveData<Task> get() = viewModel.taskLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangeBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableKeepScreenOn()
        initView()
        observeViewModel()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment() ?: return
        when (currentFragment) {
            is SelectBackgroundFragment,
            is RemoveBackgroundFragment -> doThenFinish {
                startActivity(MainActivity.newIntent(this)) }
            is BackgroundResultFragment -> showSelectBackgroundFragment()
        }
    }

    private fun initView() = with(binding) {
        errorView.setOnErrorActionButtonClickListener {
            viewModel.fetchTask()
        }
    }

    private fun observeViewModel() = with(viewModel) {
        state.observe(this@ChangeBackgroundActivity) { state ->
            applyState(state)
        }
        event.observe(this@ChangeBackgroundActivity) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }
    }

    private fun applyState(state: ChangeBackgroundState) = with(binding) {
        loadingProgress.isVisible = state.isLoading
        errorView.isVisible = state.error != null
        state.error?.let { errorView.handleError(it) }
    }

    private fun handleAction(action: ChangeBackgroundAction) = when (action) {
        ChangeBackgroundAction.ShowSelectBackground -> showSelectBackgroundFragment()
    }

    private fun showSelectBackgroundFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = SelectBackgroundFragment.TAG
    ) { SelectBackgroundFragment.newInstance(viewModel.contentMediaType) }

    private fun showRemoveBackgroundFragment(backgroundId: String?) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = RemoveBackgroundFragment.TAG
    ) { RemoveBackgroundFragment.newInstance(backgroundId, viewModel.contentMediaType) }

    private fun showBackgroundResultFragment(task: Task) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = BackgroundResultFragment.TAG
    ) { BackgroundResultFragment.newInstance(task = task, mediaType = viewModel.contentMediaType) }

    override fun onFullScreenClicked(uri: String, mediaType: MediaType) {
        startActivity(FullScreenActivity.newIntent(this, mediaType, uri))
    }

    override fun onSelectBackPressed() {
        onBackPressed()
    }

    override fun onBackgroundSelected(backgroundId: String?) {
        showRemoveBackgroundFragment(backgroundId)
    }

    override fun onTransparentSelected() {
        showBackgroundResultFragment(viewModel.taskLiveData.value ?: return)
    }

    override fun onRemovingFinished(task: Task) {
        showBackgroundResultFragment(task)
    }

    override fun onResultBackCLicked() {
        onBackPressed()
    }

    override fun onAddNewFileClicked() = doThenFinish {
        startActivity(MainActivity.newIntent(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val KEY_TASK_ID = "KEY_TASK_ID"
        private const val KEY_CONTENT_MEDIA_TYPE = "KEY_CONTENT_MEDIA_TYPE"

        fun newIntent(context: Context, taskId: String, mediaType: MediaType) =
            Intent(context, ChangeBackgroundActivity::class.java)
                .putExtra(KEY_TASK_ID, taskId)
                .putExtra(KEY_CONTENT_MEDIA_TYPE, mediaType)
    }
}