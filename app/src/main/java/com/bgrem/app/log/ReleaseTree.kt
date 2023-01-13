package com.bgrem.app.log

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber
import kotlin.math.min

class ReleaseTree : Timber.Tree() {

    companion object {
        private const val MAX_LOG_LENGTH = 4000
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean = when (priority) {
        Log.VERBOSE, Log.ASSERT -> false
        else -> true
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!isLoggable(tag, priority)) return
        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
            return
        }

        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }
}
