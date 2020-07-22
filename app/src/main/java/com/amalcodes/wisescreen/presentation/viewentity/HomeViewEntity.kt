package com.amalcodes.wisescreen.presentation.viewentity

import com.amalcodes.wisescreen.domain.entity.AppUsageEntity

data class HomeViewEntity(
    val dailyUsage: List<AppUsageEntity>,
    val totalTimeInForeground: Long
)
