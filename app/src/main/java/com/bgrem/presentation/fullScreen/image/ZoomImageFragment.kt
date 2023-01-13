package com.bgrem.presentation.fullScreen.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentZoomImageBinding
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.loadImageByUrl

class ZoomImageFragment: Fragment(R.layout.fragment_zoom_image) {

    private var _binding: FragmentZoomImageBinding? = null
    private val binding get() = _binding!!

    private val imageUriString by lazy { getExtra<String>(KEY_URI) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZoomImageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageZoom.loadImageByUrl(imageUriString!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = ZoomImageFragment::class.java.simpleName

        private const val KEY_URI = "KEY_URI"

        fun newInstance(uri: String?) = ZoomImageFragment().apply {
            arguments = bundleOf(KEY_URI to uri)
        }
    }
}