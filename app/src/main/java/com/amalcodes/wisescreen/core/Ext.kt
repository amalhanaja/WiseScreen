package com.amalcodes.wisescreen.core

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import java.util.*

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


fun Calendar.clearTime() {
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
    this[Calendar.MILLISECOND] = 0
}

fun PackageManager.getNullableApplicationInfo(
    packageName: String,
    flags: Int = 0
): ApplicationInfo? = try {
    getApplicationInfo(packageName, flags)
} catch (e: PackageManager.NameNotFoundException) {
    null
}

fun PackageManager.getApplicationName(packageName: String): String = getNullableApplicationInfo(
    packageName, PackageManager.GET_META_DATA
)?.loadLabel(this)?.toString() ?: packageName

fun PackageManager.getNullableApplicationIcon(packageName: String): Drawable? =
    getNullableApplicationInfo(packageName)?.loadIcon(this)

fun PackageManager.isSystemApp(packageName: String): Boolean =
    getNullableApplicationInfo(packageName)
        ?.let { appInfo ->
            appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                    || appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
        } ?: false

fun PackageManager.isApplicationInstalled(packageName: String): Boolean =
    getNullableApplicationInfo(packageName) != null

fun PackageManager.isOpenable(packageName: String): Boolean =
    getLaunchIntentForPackage(packageName) != null