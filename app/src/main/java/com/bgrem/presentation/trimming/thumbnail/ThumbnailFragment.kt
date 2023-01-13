package com.bgrem.presentation.trimming.thumbnail

import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentThumbnailBinding
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.trimming.TrimmingListener
import kotlinx.coroutines.delay
import java.io.File

class ThumbnailFragment :
    Fragment(R.layout.fragment_thumbnail),
    OnCommandVideoListener {

    private var _binding: FragmentThumbnailBinding? = null
    private val binding get() = _binding!!

    private val listener: TrimmingListener by lazy { getParentAsListener() }

    private val progressDialog: VideoDialog by lazy {
        VideoDialog(
            requireContext()
        )
    }

    private var destFileUri: Uri? = null
    private var destAssetFileDescriptor: AssetFileDescriptor? = null
    private var assetFileDescriptor: AssetFileDescriptor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThumbnailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = arguments?.get(KEY_URI_THUMB) as Uri?

        try {
            assetFileDescriptor = uri?.let {
                requireContext().contentResolver.openAssetFileDescriptor(
                    it,
                    AppConstants.FILE_DESCRIPTOR_MODE_READ
                )
            }
        } catch (_: Throwable){
            onError()
        }

        if (uri != null) {
            binding.videoTrimmer
                .setOnCommandListener(this)
                .setVideoURI(uri)
                .setMaxDuration(MAX_DURATION)
                .setMinDuration(MIN_DURATION)
        }

        binding.back.setOnClickListener {
            listener.onCancel()
        }

        lifecycleScope.launchWhenStarted {
            progressDialog.show()
            delay(PROGRESS_MILLIS)
            progressDialog.dismiss()
        }

        binding.save.setOnClickListener {
            try {
                destAssetFileDescriptor = getDestAssetFileDescriptor(uri)
                binding.videoTrimmer.save(assetFileDescriptor, destAssetFileDescriptor)
            } catch (_: Throwable){
                onError()
            }
        }
    }

    private fun getDestAssetFileDescriptor(uri: Uri?): AssetFileDescriptor? {
        val file: File? = null
        val mimeType = requireContext().contentResolver.getType(uri!!)
        val value = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, file?.nameWithoutExtension)
            put(
                MediaStore.Video.Media.MIME_TYPE,
                mimeType
            )
        }

        val externalUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        destFileUri =
            requireContext().contentResolver.insert(
                externalUri,
                value
            )

        return destFileUri?.let {
            requireContext().contentResolver.openAssetFileDescriptor(
                it,
                AppConstants.FILE_DESCRIPTOR_MODE_READ_AND_WRITE
            )
        }
    }

    override fun getResult() {
        progressDialog.dismiss()
        listener.onDone(destFileUri)
    }

    override fun onError() {
        showErrorSnack(resources.getString(R.string.common_error_file_not_found))
        try {
            requireContext().contentResolver.delete(
                destFileUri!!,
                null,
                null)

        } catch (_: Exception){ }
        finally {
            lifecycleScope.launchWhenStarted {
                delay(DELAY_DELETE)
                listener.onError()
            }
        }
    }

    override fun onProgress() {
        lifecycleScope.launchWhenStarted {
            progressDialog.show()
            delay(TIME_MILLIS)
            getResult()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        assetFileDescriptor = null
        destAssetFileDescriptor = null
    }

    companion object {
        private const val KEY_URI_THUMB = "KEY_URI_THUMB"
        private const val MAX_DURATION = 29
        private const val MIN_DURATION = 0
        private const val DELAY_DELETE = 1000L
        private const val TIME_MILLIS = 2000L
        private const val PROGRESS_MILLIS = 3500L

        val TAG: String = ThumbnailFragment::class.java.simpleName

        fun newInstance(uri: Uri?) = ThumbnailFragment().apply {
            arguments = bundleOf(KEY_URI_THUMB to uri)
        }
    }
}

