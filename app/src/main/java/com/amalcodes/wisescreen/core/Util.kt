package com.amalcodes.wisescreen.core

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.core.content.getSystemService
import com.amalcodes.wisescreen.R
import java.util.*
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * @author: AMAL
 * Created On : 24/07/20
 */

object Util {
    fun getDaysOfWeek(): IntRange = Calendar.SUNDAY..Calendar.SATURDAY

    fun isAppUsageStatsGranted(context: Context): Boolean {
        val appOpsManager: AppOpsManager? = context.getSystemService()
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager?.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOpsManager?.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        if (mode == AppOpsManager.MODE_DEFAULT) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun isAccessibilityServiceGranted(
        context: Context,
        serviceClass: Class<out AccessibilityService>
    ): Boolean {
        val accessibilityManager: AccessibilityManager? = context.getSystemService()
        return accessibilityManager?.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
            ?.find {
                val serviceInfo = it.resolveInfo.serviceInfo
                serviceInfo.packageName == context.packageName && serviceInfo.name == serviceClass.name
            } != null
    }

    fun formatTimeInMillis(context: Context, millis: Long): String {
        val h = MILLISECONDS.toHours(millis)
        val m = MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis))
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