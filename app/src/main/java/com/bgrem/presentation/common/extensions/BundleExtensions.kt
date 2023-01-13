package com.bgrem.presentation.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

inline fun <reified T> AppCompatActivity.getExtra(key: String): T? {
    return intent?.extras?.get(key) as? T
}

inline fun <reified T> AppCompatActivity.getExtra(key: String, default: T): T {
    return intent?.extras?.get(key) as? T ?: default
}

inline fun <reified T> Fragment.getExtra(key: String): T? {
    return arguments?.get(key) as? T
}

inline fun <reified T> Fragment.getExtra(key: String, default: T): T {
    return arguments?.get(key) as? T ?: default
}