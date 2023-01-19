package com.amalcodes.wisescreen.domain.entity

/**
 * @author: AMAL
 * Created On : 18/07/20h
 */


data class AppUsageEntity(
    val packageName: String,
    val appName: String,
    val isSystemApp: Boolean = false,
    val totalTimeInForeground: Long = 0L,
    val lastTimeUsed: Long = 0L,
    val isOpenable: Boolean = false
)

data class AppUsageStats(
    val packageName: String,
    val totalTimeInForeground: Long,
    val lastTimeUsed: Long
)