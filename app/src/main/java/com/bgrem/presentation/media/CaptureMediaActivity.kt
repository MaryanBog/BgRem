package com.bgrem.presentation.media

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bgrem.app.R
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.common.extensions.doThenFinish
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.replaceFragment
import com.bgrem.presentation.common.extensions.showErrorSnack
import com.bgrem.presentation.media.capture.CaptureFragment
import com.bgrem.presentation.media.capture.model.CaptureFragmentListener
import com.bgrem.presentation.media.preview.PreviewListener
import com.bgrem.presentation.media.preview.image.ImagePreviewFragment

class CaptureMediaActivity :
    AppCompatActivity(R.layout.activity_capture_media),
    CaptureFragmentListener,
    PreviewListener {

    private val mediaType by lazy { getExtra(KEY_MEDIA_TYPE, MediaType.IMAGE) }
    private val requiresPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted[android.Manifest.permission.CAMERA] == true) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    requestWritePermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                showCaptureFragment()
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

    private val requestWritePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                checkPermissionWriteExternalStorage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requiresPermission.launch(
            arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)
        )
    }

    private fun checkPermissionWriteExternalStorage(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if(ContextCompat.checkSelfPermission(applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                showErrorSnack(R.string.common_error_no_granted_write_storage_permission)
                requestWritePermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }


    private fun showCaptureFragment() = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = CaptureFragment.TAG
    ) { CaptureFragment.newInstance(mediaType) }

    private fun showImagePreviewFragment(uri: Uri) = replaceFragment(
        container = R.id.fragmentContainer,
        fragmentTag = ImagePreviewFragment.TAG
    ) { ImagePreviewFragment.newInstance(uri) }

    private fun getResultIntent(uri: Uri) = Intent().apply {
        data = uri
    }

    private fun getResultVideoIntent(uri: Uri) = Intent().apply {
        data = uri
    }

    override fun onImageSaved(uri: Uri) = showImagePreviewFragment(uri)

    override fun onVideoSaved(uri: Uri) = doThenFinish {
        setResult(Activity.RESULT_OK, getResultVideoIntent(uri))
    }

    override fun onRetry() = showCaptureFragment()

    override fun onCancel() = doThenFinish {
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onDone(uri: Uri) = doThenFinish {
        setResult(Activity.RESULT_OK, getResultIntent(uri))
    }

    companion object {
        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"

        fun newIntent(context: Context, mediaType: MediaType) =
            Intent(context, CaptureMediaActivity::class.java)
                .putExtra(KEY_MEDIA_TYPE, mediaType)
    }
}