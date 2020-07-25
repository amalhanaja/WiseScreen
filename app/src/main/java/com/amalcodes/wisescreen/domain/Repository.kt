package com.amalcodes.wisescreen.domain

import com.amalcodes.wisescreen.domain.entity.AppInfoEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEntity
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
}