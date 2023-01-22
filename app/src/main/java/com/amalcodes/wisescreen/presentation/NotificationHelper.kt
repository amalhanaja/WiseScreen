package com.amalcodes.wisescreen.presentation

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amalcodes.wisescreen.presentation.viewentity.NotificationEntity
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 27/07/20
 */


object NotificationHelper {
    fun notify(context: Context, data: NotificationEntity) {
        Timber.d("notify")
        val notification = buildNotification(context, data)
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                data.channelId,
                data.channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(true)
            channel.enableLights(true)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Timber.d("Permission denied")
            return
        }
        notificationManager.notify(data.tag, data.id, notification)
    }

    private fun buildNotification(context: Context, data: NotificationEntity): Notification {
        return NotificationCompat.Builder(context, data.channelId).apply {
            color = data.color
            setSmallIcon(data.smallIcon)
            setContentTitle(data.title)
            setContentText(data.message)
            setSound(data.sound)
            setTicker(data.message)
            setStyle(data.style)
            setGroup(data.groupKey)
            setContentIntent(data.pendingIntent)
            setAutoCancel(data.isAutoCancel)
            setOngoing(data.ongoing)
            setSilent(data.silent)
            setVisibility(data.visibility)
            priority = data.priority
        }.build()
    }
}