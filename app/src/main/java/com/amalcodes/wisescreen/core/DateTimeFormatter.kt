package com.amalcodes.wisescreen.core

import android.content.Context
import com.amalcodes.wisescreen.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DateTimeFormatter @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val NO_DAYS_COUNT = 0
        private const val ALL_DAYS_COUNT = 7
    }

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

    fun formatDaysOfWeek(days: List<Int>): String {
        return when (days.count()) {
            NO_DAYS_COUNT -> "-"
            ALL_DAYS_COUNT -> context.getString(R.string.text_Every_Day)
            else -> days.joinToString {
                val cal = Calendar.getInstance().apply {
                    clearTime()
                    this[Calendar.DAY_OF_WEEK] = it
                }
                cal.getDisplayName(
                    Calendar.DAY_OF_WEEK,
                    Calendar.LONG,
                    Locale.getDefault()
                ).orEmpty()
            }
        }
    }
}