package com.compose.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.data.RepositoryBgRem
import com.compose.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeComposeViewModel @Inject constructor(
    private val repository: RepositoryBgRem
): ViewModel() {

    private val _uiBackColorState = MutableStateFlow(ReplyBackColorState(loading = true))
    val uiBackColorState: StateFlow<ReplyBackColorState> = _uiBackColorState

    private val _visibilityAnimate = MutableStateFlow(true)
    val visibilityAnimate = _visibilityAnimate.asStateFlow()

    private val _sliderPosition = MutableStateFlow(0f)
    val sliderPosition = _sliderPosition.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _linkActionState = MutableStateFlow<LinkAction>(LinkAction.Start)
    val linkActionState = _linkActionState.asStateFlow()

    fun onVisibilityAnimate(visible: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            _visibilityAnimate.emit(visible)
        }
    }

    fun onPoliceLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.PoliceLink
        }
    }

    fun onTermsLink(){
        viewModelScope.launch {
            _linkActionState.value = LinkAction.Terms
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

    fun saveStateWelcomeScreen(showing: Boolean){
        repository.saveStateWelcomeScreen(showing)
    }
    fun getStateWelcomeScreen(): Boolean{
        return  repository.getStateWelcomeScreen()
    }

    fun onSliderPosition(sliderPosition: Float){
        viewModelScope.launch(Dispatchers.IO) {
            _sliderPosition.emit(sliderPosition)
        }
    }

    fun observeBackColor() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBackColor()
                .catch { ex ->
                    _uiBackColorState.value = ReplyBackColorState(error = ex.message)
                }
                .collect { backColor ->
                    _uiBackColorState.value = ReplyBackColorState(backColor = backColor)
                }
        }
    }
}