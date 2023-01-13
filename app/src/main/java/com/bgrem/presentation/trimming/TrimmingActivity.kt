package com.bgrem.presentation.trimming

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bgrem.app.R
import com.bgrem.presentation.common.extensions.doThenFinish
import com.bgrem.presentation.trimming.thumbnail.ThumbnailFragment

class TrimmingActivity :
    AppCompatActivity(R.layout.activity_trimming),
    TrimmingListener {

    private val uri by lazy { intent.extras?.get(KEY_URI_TRIM) as Uri}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showThumbnailFragment(uri)
    }

    private fun showThumbnailFragment(uri: Uri?){
        supportFragmentManager.commit {
            replace(
                R.id.fragmentContainer,
                ThumbnailFragment.newInstance(uri)
            )
        }
    }

    private fun getResultIntent(uri: Uri?) = Intent().apply {
        data = uri
    }

    override fun onCancel() = doThenFinish {
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onDone(uri: Uri?) = doThenFinish {
        setResult(Activity.RESULT_OK, getResultIntent(uri))
    }

    override fun onError() = doThenFinish{
        setResult(Activity.RESULT_CANCELED)
    }

    companion object {
        private const val KEY_URI_TRIM = "KEY_URI_TRIM"

        fun newIntent(context: Context, uri: Uri?) =
            Intent(context, TrimmingActivity::class.java)
                .putExtra(KEY_URI_TRIM, uri)
    }
}
