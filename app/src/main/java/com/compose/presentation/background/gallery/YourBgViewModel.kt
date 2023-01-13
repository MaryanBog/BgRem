package com.compose.presentation.background.gallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.presentation.gallery.video.VideoAction
import com.compose.utils.MimeTypesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourBgViewModel @Inject constructor(
    private val mimeTypes: MimeTypesUtils
): ViewModel() {

    val imageMimeTypes: List<String> get() = mimeTypes.getAvailableImageTypes()
    val videoMimeTypes: List<String> get() = mimeTypes.getAvailableVideoTypes()

    private val _visibleDialog = MutableStateFlow(false)
    val visibleDialog = _visibleDialog.asStateFlow()

    private val _yourBgAction = MutableStateFlow<YourBgAction>(YourBgAction.Start)
    val yourBgAction = _yourBgAction.asStateFlow()

    private val _openImageState = MutableStateFlow<Boolean?>(null)
    val openImageState = _openImageState.asStateFlow()

    private val _openVideoState = MutableStateFlow<Boolean?>(null)
    val openVideoState = _openVideoState.asStateFlow()

    fun onAddImage(uri: Uri?){
        viewModelScope.launch(Dispatchers.IO){
            _yourBgAction.value = YourBgAction.AddImage(uri)
        }
    }

    fun onAddVideo(uri: Uri?){
        if (uri != null) {
            viewModelScope.launch {
                _yourBgAction.value = YourBgAction.AddVideo(uri)
            }
        } else return
    }

    fun onVisibleDialog(visible: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            _visibleDialog.emit(visible)
        }
    }

    fun onOpenVideo(state: Boolean?){
        viewModelScope.launch(Dispatchers.IO){
            _openVideoState.emit(state)
        }
    }

    fun onOpenImage(state: Boolean?){
        viewModelScope.launch(Dispatchers.IO) {
            _openImageState.emit(state)
        }
    }

}