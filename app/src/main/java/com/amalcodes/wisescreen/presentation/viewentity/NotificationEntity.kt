package com.amalcodes.wisescreen.presentation.viewentity

import android.app.PendingIntent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.NotificationVisibility
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 27/07/20
 */


data class NotificationEntity(
    val tag: String? = null,
    val color: Int = NotificationCompat.COLOR_DEFAULT,
    val message: String? = null,
    val id: Int = 0,
    val channelId: String,
    val channelName: String,
    val title: String,
    val isAutoCancel: Boolean = true,
    val sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
    val style: NotificationCompat.Style? = null,
    val pendingIntent: PendingIntent? = null,
    val groupKey: String? = null,
    val smallIcon: Int = R.mipmap.ic_launcher,
    val silent: Boolean = false,
    val ongoing: Boolean = false,
    @NotificationVisibility
    val visibility: Int = NotificationCompat.VISIBILITY_PUBLIC,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT
)