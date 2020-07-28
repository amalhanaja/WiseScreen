package com.amalcodes.wisescreen.domain

import android.content.pm.ApplicationInfo
import com.amalcodes.wisescreen.domain.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 17/07/20
 */


interface Repository {
    fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>>
    fun getScreenTimeConfig(): Flow<ScreenTimeConfigEntity>
    fun saveScreenTimeConfig(config: ScreenTimeConfigEntity): Flow<Unit>
    fun getApplicationList(): Flow<List<AppInfoEntity>>
    fun setPin(pin: String): Flow<Unit>
    fun getPin(): Flow<String>
}