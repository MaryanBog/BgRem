package com.bgrem.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.launch.SetFirstLaunchedUseCase
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val setFirstLaunchedUseCase: SetFirstLaunchedUseCase
) : ViewModel() {

    fun onGetStartedClicked() {
        viewModelScope.launch {
            setFirstLaunchedUseCase()
        }
    }
}