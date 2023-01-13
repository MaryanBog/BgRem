package com.bgrem.presentation.media.preview.image

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentImagePreviewBinding
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.media.preview.PreviewListener

class ImagePreviewFragment : Fragment(R.layout.fragment_image_preview) {

    private var _binding: FragmentImagePreviewBinding? = null
    private val binding get() = _binding!!

    private val previewListener by lazy { getParentAsListener<PreviewListener>() }
    private val imageUri by lazy { getExtra<Uri>(KEY_URI) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePreviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        previewImage.setImageURI(imageUri)

        doneImage.setOnClickListener {
            imageUri?.let { previewListener.onDone(it) }
        }
        replyImage.setOnClickListener {
            previewListener.onRetry()
        }
        closeImage.setOnClickListener {
            previewListener.onCancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = ImagePreviewFragment::class.java.simpleName

        private const val KEY_URI = "KEY_URI"

        fun newInstance(uri: Uri) = ImagePreviewFragment().apply {
            arguments = bundleOf(KEY_URI to uri)
        }
    }
}