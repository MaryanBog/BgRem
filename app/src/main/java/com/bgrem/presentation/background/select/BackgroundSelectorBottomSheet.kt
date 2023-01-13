package com.bgrem.presentation.background.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bgrem.app.R
import com.bgrem.app.databinding.BottomSheetBackgroundSelectorBinding
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class BackgroundSelectorBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetBackgroundSelectorBinding::bind)
    private val listener by lazy { getParentAsListener<BackgroundSelectorListener>() }
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_background_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        initView()
    }

    private fun initView() = with(binding) {
        photoText.setOnClickListener {
            listener.onPhotoClick()
            firebaseAnalytics.logEvent("Upload_Photo_Android", Bundle().apply {
                this.putString(
                    KEY_SELECT, "upload_user_photo_as_background"
                )
            })
            dismiss()
        }

        videoText.setOnClickListener {
            listener.onVideoClick()
            firebaseAnalytics.logEvent("Upload_Video_Android", Bundle().apply {
                this.putString(
                    KEY_SELECT, "upload_user_video_as_background"
                )
            })
            dismiss()
        }
    }

    interface BackgroundSelectorListener {
        fun onPhotoClick()
        fun onVideoClick()
    }

    companion object {
        val TAG: String = BackgroundSelectorBottomSheet::class.java.simpleName
        private const val KEY_SELECT = "KEY_SELECT"

        fun newInstance() = BackgroundSelectorBottomSheet()
    }
}