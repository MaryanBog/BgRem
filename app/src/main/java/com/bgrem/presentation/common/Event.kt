package com.bgrem.presentation.common

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    @Synchronized
    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}