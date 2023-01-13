package com.bgrem.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bgrem.presentation.common.extensions.asLiveData

abstract class StateViewModel<S, A>(defaultState: S? = null) : ViewModel() {
    protected val _state = MutableLiveData<S>().apply {
        defaultState?.let { value = it }
    }
    val state = _state.asLiveData()

    protected val _event = MutableLiveData<Event<A>>()
    val event = _event.asLiveData()
}