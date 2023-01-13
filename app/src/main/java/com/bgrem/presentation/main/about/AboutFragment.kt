package com.bgrem.presentation.main.about

import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bgrem.app.BuildConfig
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentAboutBinding
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.launchIntentIfAvailable
import com.bgrem.presentation.common.extensions.loadGifDrawable
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.common.utils.IntentUtils
import com.bgrem.presentation.common.utils.SpannableUtils

class AboutFragment : Fragment(R.layout.fragment_about) {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val listener: AboutClickListener by lazy { getParentAsListener() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        infoGifImageView.loadGifDrawable(R.drawable.gif_info)
        versionText.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)

        sloganText.text = SpannableUtils.changeTypefaceOfSubstring(
            typeface = Typeface.BOLD,
            text = getString(R.string.about_slogan),
            subText = listOf(getString(R.string.about_slogan_bold_part))
        )

        contactEmailText.apply {
            text = SpannableUtils.makeTextWithClickablePart(
                text = getString(R.string.about_contact_email),
                clickableParts = listOf(
                    getString(R.string.common_support_email) to ::showEmailSendingForm
                ),
                clickablePartColor = ContextCompat.getColor(requireContext(), R.color.accent_blue)
            )
            movementMethod = LinkMovementMethod.getInstance()
        }

        aboutToolbar.setNavigationOnClickListener {
            listener.onBackClicked()
        }

        privacyPolicyText.setOnClickListener {
            openPrivacyPolicy()
        }
    }

    private fun openPrivacyPolicy() =
        launchIntentIfAvailable(IntentUtils.getBrowserIntent(getString(R.string.privacy_policy))) {
            showErrorSnack(R.string.common_error_not_found_app_on_device)
        }

    private fun showEmailSendingForm() =
        launchIntentIfAvailable(IntentUtils.getEmailSendIntent(getString(R.string.common_support_email))) {
            showErrorSnack(R.string.common_error_email_client_not_found)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG = AboutFragment::class.java.simpleName

        fun newInstance() = AboutFragment()
    }
}