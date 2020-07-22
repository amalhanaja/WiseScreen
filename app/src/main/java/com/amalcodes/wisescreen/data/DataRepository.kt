package com.amalcodes.wisescreen.data

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.amalcodes.wisescreen.core.*
import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageStats
import com.amalcodes.wisescreen.domain.entity.TimeRangeEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject


/**
 * @author: AMAL
 * Created On : 17/07/20
 */


class DataRepository @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    @ApplicationContext private val context: Context
) : Repository {
    override fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>> {
        val (start, end) = timeRange
        val usages = usageStatsManager.queryAndAggregateEvents(start, end)
        val items = usages.map { (key, value) ->
            val appInfo = packageManager.getNullableApplicationInfo(key)
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

    //    override fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>> = flow {
//        // Get the app statistics since one year ago from the current time.
//        val (start, end) = timeRange
//        val usageEvents: UsageEvents = usageStatsManager.queryEvents(start, end)
//        val items = mutableListOf<AppUsageEntity>()
//        val startPoints: MutableMap<String, Long> = mutableMapOf()
//        val endPoints: MutableMap<String, UsageEvents.Event> = mutableMapOf()
//        var prevPackageName = ""
//        while (usageEvents.hasNextEvent()) {
//            val event = UsageEvents.Event()
//            usageEvents.getNextEvent(event)
//            val eventType = event.eventType
//            val eventTime = event.timeStamp
//            val eventPackageName = event.packageName
//            if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
//                if (items.notContainsPackageName(eventPackageName)) {
//                    items.add(
//                        AppUsageEntity(
//                            packageName = eventPackageName,
//                            appName = packageManager.getApplicationName(eventPackageName),
//                            appIcon = packageManager.getNullableApplicationIcon(eventPackageName),
//                            isSystemApp = packageManager.isSystemApp(eventPackageName)
//                        )
//                    )
//                }
//                if (!startPoints.containsKey(eventPackageName)) {
//                    startPoints[eventPackageName] = eventTime
//                }
//            }
//            if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
//                if (startPoints.isNotEmpty() && startPoints.containsKey(eventPackageName)) {
//                    endPoints[eventPackageName] = event
//                }
//            }
//            if (prevPackageName.isEmpty()) prevPackageName = eventPackageName
//            if (prevPackageName != eventPackageName) {
//                if (
//                    startPoints.containsKey(prevPackageName)
//                    && endPoints.containsKey(prevPackageName)
//                ) {
//                    val lastEndEvent = endPoints.getValue(prevPackageName)
//                    if (items.containsPackageName(prevPackageName)) {
//                        val item = items.first { it.packageName == prevPackageName }
//                        item.lastTimeUsed = lastEndEvent.timeStamp
//                        var duration =
//                            lastEndEvent.timeStamp - startPoints.getValue(prevPackageName)
//                        Timber.d("$duration")
//                        if (duration <= 0L) duration = 0
//                        item.totalTimeInForeground += duration
//                    }
//                    startPoints.remove(prevPackageName)
//                    endPoints.remove(prevPackageName)
//                }
//                prevPackageName = eventPackageName
//            }
//        }
//        emit(items.sortedByDescending { it.totalTimeInForeground })
//    }

    private fun List<AppUsageEntity>.notContainsPackageName(packageName: String): Boolean {
        return none { it.packageName == packageName }
    }

    private fun List<AppUsageEntity>.containsPackageName(packageName: String): Boolean {
        return any { it.packageName == packageName }
    }
}