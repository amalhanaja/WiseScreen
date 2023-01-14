package com.amalcodes.wisescreen.core

import android.content.Context
import com.amalcodes.wisescreen.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DateTimeFormatter @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun formatTimeInMillis(millis: Long): String {
        val h = TimeUnit.MILLISECONDS.toHours(millis)
        val m = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
        if (h == 0L && m == 0L) {
            return "< ${context.getString(R.string.text_minute, m)}"
        }
        val stringBuilder = StringBuilder()
        if (h != 0L) {
            stringBuilder.append(context.getString(R.string.text_hour, h))
                .append(" ")
        }
        stringBuilder.append(context.getString(R.string.text_minute, m))
        return stringBuilder.toString()
    }
}