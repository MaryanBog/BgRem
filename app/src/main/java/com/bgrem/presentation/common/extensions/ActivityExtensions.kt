package com.bgrem.presentation.common.extensions

import android.app.Activity
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.bgrem.app.R
import com.bgrem.presentation.common.utils.SnackBarUtils

inline fun Activity.doThenFinish(block: () -> Unit) {
    block.invoke()
    finish()
}

fun AppCompatActivity.replaceFragment(
    @IdRes
    container: Int,
    fragmentTag: String? = null,
    addToBackStack: Boolean = false,
    block: () -> Fragment,
) {
    supportFragmentManager
        .commit {
            if (addToBackStack) addToBackStack(fragmentTag)
            val fragment = supportFragmentManager.findFragmentByTag(fragmentTag) ?: block.invoke()
            replace(container, fragment, fragmentTag)
        }
}

fun Activity.showErrorSnack(message: String) = SnackBarUtils.showSnack(
    view = window.decorView,
    message = message,
    bgColor = getColor(R.color.accent_red),
    textColor = getColor(R.color.base_white)
)

fun Activity.showErrorSnack(@StringRes messageRes: Int) = showErrorSnack(getString(messageRes))


fun FragmentActivity.getCurrentFragment(): Fragment? =
    supportFragmentManager.fragments.find { it.isVisible }


fun Activity.enableKeepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Activity.disableKeepScreenOn() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}