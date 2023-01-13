package com.compose.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.presentation.welcome.LinkAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutComposeViewModel @Inject constructor(): ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _linkActionState = MutableStateFlow<LinkAction>(LinkAction.Start)
    val linkActionState = _linkActionState.asStateFlow()

    fun onPoliceLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.PoliceLink
        }
    }

    fun onFacebookLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.Facebook
        }
    }

    fun onInstagramLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.Instagram
        }
    }

    fun onYoutubeLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.Youtube
        }
    }

    fun onErrorShow(stringRes: String?){
        viewModelScope.launch {
            if (stringRes == null){
                _errorMessage.emit(null)
            } else {
                _errorMessage.emit(stringRes)
            }
        }
    }

    fun onSendEmail(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.EmailLink
        }
    }
}