package com.amalcodes.wisescreen.core.initializer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import androidx.core.content.getSystemService
import androidx.startup.Initializer
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.presentation.receiver.AlarmReceiver

/**
 * @author: AMAL
 * Created On : 27/07/20
 */


class AppUsageMonitorInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AlarmReceiver.startAlarm(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}