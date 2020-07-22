package com.amalcodes.wisescreen.domain

import com.amalcodes.wisescreen.domain.entity.TimeRangeEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 17/07/20
 */


interface Repository {
    fun getUsageStats(timeRange: TimeRangeEntity): Flow<List<AppUsageEntity>>
}