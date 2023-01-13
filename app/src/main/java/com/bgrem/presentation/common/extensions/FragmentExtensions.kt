package com.bgrem.presentation.common.extensions

import android.content.Intent
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.bgrem.app.R
import com.bgrem.presentation.common.utils.SnackBarUtils

object FragmentListenerExtensions {
    fun <T> getFragmentListenerOrNull(fragment: Fragment, listenerClass: Class<T>): T? {
        val parentFragment = fragment.parentFragment
        val parentActivity = fragment.activity

        return when {
            listenerClass.isInstance(parentFragment) -> listenerClass.cast(parentFragment)
            listenerClass.isInstance(parentActivity) -> listenerClass.cast(parentActivity)
            else -> null
        }
    }

    fun <T> getFragmentListener(fragment: Fragment, listenerClass: Class<T>): T {
        return getFragmentListenerOrNull(fragment, listenerClass)
            ?: throw Exception("${fragment.activity} or ${fragment.parentFragment} must implement $listenerClass")
    }
}

inline fun <reified T> Fragment.getParentAsListener() =
    FragmentListenerExtensions.getFragmentListener(this, T::class.java)


fun Fragment.showErrorSnack(message: String) = SnackBarUtils.showSnack(
    view = requireView(),
    message = message,
    bgColor = requireContext().getColor(R.color.accent_red),
    textColor = requireContext().getColor(R.color.base_white)
)

fun Fragment.showErrorSnack(@StringRes messageRes: Int) = showErrorSnack(getString(messageRes))

fun Fragment.showSuccessSnack(message: String) = SnackBarUtils.showSnack(
    view = requireView(),
    message = message,
    bgColor = requireContext().getColor(R.color.accent_green),
    textColor = requireContext().getColor(R.color.base_white)
)

fun Fragment.showSuccessSnack(@StringRes messageRes: Int) = showSuccessSnack(getString(messageRes))

fun Fragment.launchIntentIfAvailable(intent: Intent, onUnavailable: () -> Unit) {
    intent.resolveActivity(requireContext().packageManager)?.let {
        startActivity(intent)
    } ?: onUnavailable.invoke()
}