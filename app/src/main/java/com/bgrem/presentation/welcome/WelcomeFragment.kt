package com.bgrem.presentation.welcome

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentWelcomeBinding
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.launchIntentIfAvailable
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.common.utils.IntentUtils
import com.bgrem.presentation.common.utils.SpannableUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModel()

    private val changeFragmentListener by lazy { getParentAsListener<ChangeFragmentListener>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        welcomeCaptionText.text = SpannableUtils.makeTextWithClickablePart(
            text = getString(R.string.welcome_caption),
            clickableParts = listOf(
                getString(R.string.welcome_terms_and_condition) to ::openTermsAndConditions,
                getString(R.string.welcome_privacy_policy) to ::openPrivacyPolicy
            ),
            clickablePartColor = ContextCompat.getColor(requireContext(), R.color.accent_blue)
        )
        welcomeCaptionText.movementMethod = LinkMovementMethod.getInstance()

        welcomeStartButton.setOnClickListener {
            viewModel.onGetStartedClicked()
            changeFragmentListener.onGetStartClicked()
        }

        welcomeInfoImage.setOnClickListener {
            changeFragmentListener.onGetInfoClicked()
        }
    }

    private fun openTermsAndConditions() =
        launchIntentIfAvailable(IntentUtils.getBrowserIntent(getString(R.string.terms_of_use))) {
            showErrorSnack(R.string.common_error_not_found_app_on_device)
        }

    private fun openPrivacyPolicy() =
        launchIntentIfAvailable(IntentUtils.getBrowserIntent(getString(R.string.privacy_policy))) {
            showErrorSnack(R.string.common_error_not_found_app_on_device)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = WelcomeFragment::class.java.simpleName

        fun newInstance() = WelcomeFragment()
    }
}
