package com.amalcodes.wisescreen.domain.entity

/**
 * @author: AMAL
 * Created On : 23/07/20
 */
 
 
data class ScreenTimeConfigEntity(
    val workingDayDailyScreenTimeInMillis: Long = 0,
    val workingDays: List<Int>,
    val restDayDailyScreenTimeInMillis: Long = 0
)