package com.bgrem.presentation.main.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentSplashBinding
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.loadGifDrawable

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val listener by lazy { getParentAsListener<SplashListener>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        splashGifImage.loadGifDrawable(
            drawableRes = R.drawable.animated_logo,
            loopCount = 1
        ) {
            listener.onSplashAnimationEnded()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName

        fun newInstance() = SplashFragment()
    }
}