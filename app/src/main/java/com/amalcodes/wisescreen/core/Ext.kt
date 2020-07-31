package com.amalcodes.wisescreen.core

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.MotionEvent
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.IntDef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.util.*

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


fun Calendar.clearTime() {
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MILLISECOND] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
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

fun PackageManager.getNullableActivityInfo(
    componentName: ComponentName,
    flags: Int = 0
): ActivityInfo? = try {
    getActivityInfo(componentName, flags)
} catch (e: PackageManager.NameNotFoundException) {
    null
}

fun PackageManager.isActivity(componentName: ComponentName): Boolean =
    getNullableActivityInfo(componentName) != null

fun PackageManager.isOpenable(packageName: String): Boolean =
    getLaunchIntentForPackage(packageName) != null

fun Calendar.setMs(ms: Int) {
    clearTime()
    add(Calendar.MILLISECOND, ms)
}

var TimePicker.millis: Int
    set(value) {
        val cal = Calendar.getInstance().apply { setMs(value) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = cal[Calendar.HOUR_OF_DAY]
        } else {
            @Suppress("DEPRECATION")
            currentHour = cal[Calendar.HOUR_OF_DAY]
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            minute = cal[Calendar.MINUTE]
        } else {
            @Suppress("DEPRECATION")
            currentMinute = cal[Calendar.MINUTE]
        }
    }
    get() {
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour
        } else {
            @Suppress("DEPRECATION")
            currentHour
        } * 3_600_000
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            minute
        } else {
            @Suppress("DEPRECATION")
            currentMinute
        } * 60_000
        return hour + minute
    }

@ExperimentalCoroutinesApi
fun <T1, T2, T3, R> Flow<T1>.zip3(
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    transform: suspend (T1, T2, T3) -> R
): Flow<R> = zip(flow2) { t1, t2 -> t1 to t2 }
    .zip(flow3) { (t1, t2), t3 -> transform(t1, t2, t3) }


@IntDef(
    Const.DRAWABLE_LEFT,
    Const.DRAWABLE_BOTTOM,
    Const.DRAWABLE_RIGHT,
    Const.DRAWABLE_TOP
)
annotation class CompoundDrawablePosition

@SuppressLint("ClickableViewAccessibility")
fun TextView.onCompoundDrawableClickListener(
    @CompoundDrawablePosition drawablePosition: Int,
    onClick: () -> Unit
) {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val isClicked = when (drawablePosition) {
                Const.DRAWABLE_LEFT -> compoundDrawables.getOrNull(Const.DRAWABLE_LEFT) != null
                        && event.rawX <= (compoundDrawables[Const.DRAWABLE_LEFT].bounds.width())
                Const.DRAWABLE_RIGHT -> compoundDrawables.getOrNull(Const.DRAWABLE_RIGHT) != null
                        && event.rawX >= (right - compoundDrawables[Const.DRAWABLE_RIGHT].bounds.width())
                else -> false
            }
            if (isClicked) {
                onClick.invoke()
                return@setOnTouchListener true
            }
        }
        false;
    }
}