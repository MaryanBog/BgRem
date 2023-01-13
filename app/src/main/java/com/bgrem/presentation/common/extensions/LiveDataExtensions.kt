package com.bgrem.presentation.common.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.bgrem.presentation.common.Event

fun <T> MutableLiveData<Event<T>>.postEvent(value: T?) {
    value?.let { postValue(Event(value)) }
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this.map { it }
}