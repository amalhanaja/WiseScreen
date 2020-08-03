package com.amalcodes.wisescreen.data

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.edit
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.getNullableApplicationIcon
import com.amalcodes.wisescreen.core.isOpenable
import com.amalcodes.wisescreen.core.isSystemApp
import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.*
import javax.inject.Inject


/**
 * @author: AMAL
 * Created On : 17/07/20
 */


class DataRepository @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val sharedPreferences: SharedPreferences
) : Repository {

    override fun getPin(): Flow<String> = flow {
        emit(sharedPreferences.getString("PIN", "").orEmpty())
    }

    override fun setPin(pin: String): Flow<Unit> = flow {
        emit(sharedPreferences.edit { putString("PIN", pin) })
    }

    override fun getApplicationList(): Flow<List<AppInfoEntity>> {
        val list: List<AppInfoEntity> = packageManager.getInstalledApplications(
            PackageManager.GET_META_DATA
        ).filter {
            packageManager.isOpenable(it.packageName)
        }.sortedBy {
            packageManager.getApplicationName(it.packageName)
        }.map { AppInfoEntity(packageName = it.packageName) }
        return flowOf(list)
    }

    override fun saveScreenTimeConfig(config: ScreenTimeConfigEntity): Flow<Unit> {
        sharedPreferences.edit {
            putStringSet("WORKING_DAYS", config.workingDays.map { it.toString() }.toSet())
            putInt("WORKING_DAYS_SCREEN_TIME", config.workingDayDailyScreenTimeInMillis)
            putInt("REST_DAYS_SCREEN_TIME", config.restDayDailyScreenTimeInMillis)
            putBoolean("IS_SCREEN_TIME_MANAGEABLE", config.isScreenTimeManageable)
        }
        return flowOf(Unit)
    }

    override fun getScreenTimeConfig(): Flow<ScreenTimeConfigEntity> {
        val workingDays = sharedPreferences.getStringSet(
            "WORKING_DAYS",
            (Calendar.MONDAY..Calendar.FRIDAY).map(Int::toString).toSet()
        )!!.map(String::toInt)
        return flowOf(
            ScreenTimeConfigEntity(
                workingDays = workingDays,
                workingDayDailyScreenTimeInMillis = sharedPreferences.getInt(
                    "WORKING_DAYS_SCREEN_TIME",
                    6 * 3_600_000
                ),
                restDayDailyScreenTimeInMillis = sharedPreferences.getInt(
                    "REST_DAYS_SCREEN_TIME",
                    6 * 3_600_000
                ),
                isScreenTimeManageable = sharedPreferences.getBoolean(
                    "IS_SCREEN_TIME_MANAGEABLE",
                    false
                )
            )
        )
    }

    override fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>> {
        val (start, end) = timeRange
        val usages = usageStatsManager.queryAndAggregateEvents(start, end)
        val items = usages.map { (key, value) ->
            AppUsageEntity(
                packageName = key,
                appName = packageManager.getApplicationName(key),
                appIcon = packageManager.getNullableApplicationIcon(key),
                isSystemApp = packageManager.isSystemApp(key),
                totalTimeInForeground = value.totalTimeInForeground,
                isOpenable = packageManager.isOpenable(key)
            )
        }.sortedByDescending { it.totalTimeInForeground }
        return flowOf(items)
    }

    private fun UsageStatsManager.queryAndAggregateEvents(
        start: Long,
        end: Long
    ): MutableMap<String, AppUsageStats> {
        val events = queryEvents(start, end)
        val aggregateMap = mutableMapOf<String, AppUsageStats>()
        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            val appUsageStat = aggregateMap[event.packageName]
            appUsageStat?.let {
                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_PAUSED -> {
                        aggregateMap[event.packageName] = it.copy(
                            lastTimeUsed = event.timeStamp,
                            totalTimeInForeground = it.totalTimeInForeground + (event.timeStamp - it.lastTimeUsed)
                        )
                    }
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        aggregateMap[event.packageName] = it.copy(
                            lastTimeUsed = event.timeStamp
                        )
                    }
                }
            } ?: {
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    aggregateMap[event.packageName] = AppUsageStats(
                        packageName = event.packageName,
                        totalTimeInForeground = 0,
                        lastTimeUsed = event.timeStamp
                    )
                }
            }()
        }
        return aggregateMap
    }
}