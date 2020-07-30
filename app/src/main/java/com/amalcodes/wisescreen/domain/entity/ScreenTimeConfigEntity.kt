package com.amalcodes.wisescreen.domain.entity

/**
 * @author: AMAL
 * Created On : 23/07/20
 */
 
 
data class ScreenTimeConfigEntity(
    val workingDayDailyScreenTimeInMillis: Int = 0,
    val workingDays: List<Int>,
    val isScreenTimeManageable: Boolean,
    val restDayDailyScreenTimeInMillis: Int = 0
)