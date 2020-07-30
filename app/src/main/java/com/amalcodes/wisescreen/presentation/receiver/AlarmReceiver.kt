package com.amalcodes.wisescreen.presentation.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.content.getSystemService
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.presentation.worker.UsageNotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 27/07/20
 */


@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private fun getIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }

        fun startAlarm(context: Context) {
            val alarmManager: AlarmManager = requireNotNull(context.getSystemService())
            val alarmReceiverIntent = getIntent(context)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                Const.REQUEST_CODE_DEFAULT,
                alarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                60000L,
                pendingIntent
            )
        }

        fun stopAlarm(context: Context) {
            val alarmManager: AlarmManager = requireNotNull(context.getSystemService())
            val intent = getIntent(context)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                Const.REQUEST_CODE_DEFAULT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            Timber.d("Alarm Canceled")
            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive, ${intent?.action}")
        context?.let { UsageNotificationWorker.enqueue(it) }
    }
}