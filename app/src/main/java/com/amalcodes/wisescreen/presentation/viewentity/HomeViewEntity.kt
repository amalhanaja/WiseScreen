package com.amalcodes.wisescreen.presentation.viewentity

import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity

data class HomeViewEntity(
    val dailyUsage: List<AppUsageEntity>,
    val totalTimeInForeground: Long,
    val isPinSet: Boolean,
    val screenTimeConfigEntity: ScreenTimeConfigEntity
)
