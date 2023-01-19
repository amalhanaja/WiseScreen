package com.amalcodes.wisescreen.data

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.isOpenable
import com.amalcodes.wisescreen.core.isSystemApp
import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.AppInfoEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageStats
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject


/**
 * @author: AMAL
 * Created On : 17/07/20
 */


class DataRepository @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val dataStore: DataStore<Preferences>,
) : Repository {

    companion object {
        private const val DEFAULT_SCREEN_TIME_LIMIT = 6 * 3_600_000
        private val KEY_PIN = stringPreferencesKey("PIN")
        private val KEY_WORKING_DAYS = stringSetPreferencesKey("WORKING_DAYS")
        private val KEY_WORKING_DAYS_SCREEN_TIME = intPreferencesKey("WORKING_DAYS_SCREEN_TIME")
        private val KEY_REST_DAYS_SCREEN_TIME = intPreferencesKey("REST_DAYS_SCREEN_TIME")
        private val KEY_IS_SCREEN_TIME_MANAGEABLE = booleanPreferencesKey("IS_SCREEN_TIME_MANAGEABLE")
    }

    override fun getPin(): Flow<String> {
        return dataStore.data.map { value: Preferences -> value[KEY_PIN].orEmpty() }
    }

    override suspend fun setPin(pin: String) {
        dataStore.edit { mutablePreferences -> mutablePreferences[KEY_PIN] = pin }
    }

    override fun getApplicationList(): Flow<List<AppInfoEntity>> = flow {
        val list: List<AppInfoEntity> = packageManager.getInstalledApplications(
            PackageManager.GET_META_DATA
        ).filter {
            packageManager.isOpenable(it.packageName)
        }.sortedBy {
            packageManager.getApplicationName(it.packageName)
        }.map { AppInfoEntity(packageName = it.packageName) }
        emit(list)
    }

    override suspend fun saveScreenTimeConfig(config: ScreenTimeConfigEntity) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_WORKING_DAYS] = config.workingDays.map(Int::toString).toSet()
            mutablePreferences[KEY_WORKING_DAYS_SCREEN_TIME] = config.workingDayDailyScreenTimeInMillis
            mutablePreferences[KEY_REST_DAYS_SCREEN_TIME] = config.restDayDailyScreenTimeInMillis
            mutablePreferences[KEY_IS_SCREEN_TIME_MANAGEABLE] = config.isScreenTimeManageable
        }
    }

    override fun getScreenTimeConfig(): Flow<ScreenTimeConfigEntity> {
        val defaultWorkingDays = (Calendar.MONDAY..Calendar.FRIDAY).toList()
        return dataStore.data.map { preferences ->
            ScreenTimeConfigEntity(
                workingDays = preferences[KEY_WORKING_DAYS]?.map(String::toInt) ?: defaultWorkingDays,
                workingDayDailyScreenTimeInMillis = preferences[KEY_WORKING_DAYS_SCREEN_TIME] ?: DEFAULT_SCREEN_TIME_LIMIT,
                restDayDailyScreenTimeInMillis = preferences[KEY_REST_DAYS_SCREEN_TIME] ?: DEFAULT_SCREEN_TIME_LIMIT,
                isScreenTimeManageable = preferences[KEY_IS_SCREEN_TIME_MANAGEABLE] ?: false,
            )
        }
    }

    override fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>> = flow {
        val (start, end) = timeRange
        val usages = usageStatsManager.queryAndAggregateEvents(start, end)
        val items = usages.map { (key, value) ->
            AppUsageEntity(
                packageName = key,
                appName = packageManager.getApplicationName(key),
                isSystemApp = packageManager.isSystemApp(key),
                totalTimeInForeground = value.totalTimeInForeground,
                isOpenable = packageManager.isOpenable(key)
            )
        }.sortedByDescending { it.totalTimeInForeground }
        emit(items)
    }

    private fun UsageStatsManager.queryAndAggregateEvents(
        start: Long,
        end: Long,
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
            } ?: run {
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    aggregateMap[event.packageName] = AppUsageStats(
                        packageName = event.packageName,
                        totalTimeInForeground = 0,
                        lastTimeUsed = event.timeStamp
                    )
                }
                Unit
            }
        }
        return aggregateMap
    }
}