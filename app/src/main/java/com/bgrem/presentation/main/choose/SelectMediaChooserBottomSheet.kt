package com.bgrem.presentation.main.choose

import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bgrem.app.R
import com.bgrem.app.databinding.BottomSheetSelectMediaChooserBinding
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.common.utils.SpannableUtils
import com.bgrem.domain.common.media.MediaType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectMediaChooserBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetSelectMediaChooserBinding::bind)
    private val mediaType by lazy { getExtra(KEY_MEDIA_TYPE, MediaType.IMAGE) }
    private val listener by lazy { getParentAsListener<SelectMediaChooserListener>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_select_media_chooser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener.onDismiss()
        super.onDismiss(dialog)
    }

    private fun initView() = with(binding) {
        noteText.apply {
            isVisible = mediaType == MediaType.VIDEO
            text = SpannableUtils.changeTypefaceOfSubstring(
                typeface = Typeface.BOLD,
                text = context.getString(R.string.choose_media_note),
                subText = listOf(context.getString(R.string.choose_media_note_title))
            )
        }

        galleryText.setOnClickListener {
            listener.onGalleryClick()
            dismiss()
        }

        cameraText.setOnClickListener {
            listener.onCameraClick()
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    interface SelectMediaChooserListener {
        fun onCameraClick()
        fun onGalleryClick()
        fun onDismiss()
    }

    companion object {
        val TAG: String = SelectMediaChooserBottomSheet::class.java.simpleName

        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"

        fun create(mediaType: MediaType) = SelectMediaChooserBottomSheet().apply {
            arguments = bundleOf(KEY_MEDIA_TYPE to mediaType)
        }
    }
}