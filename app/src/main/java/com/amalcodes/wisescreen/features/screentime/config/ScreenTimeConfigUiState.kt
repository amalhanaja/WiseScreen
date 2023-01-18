package com.amalcodes.wisescreen.features.screentime.config

sealed interface ScreenTimeConfigUiState {
    object NotShown : ScreenTimeConfigUiState
    data class Success(
        val formattedWorkDays: String,
        val formattedWorkDayScreenTime: String,
        val formattedRestDays: String,
        val formattedRestDayScreenTime: String,
        val workDays: List<Int>,
        val restDays: List<Int>,
        val workingDayScreenTime: Int,
        val restDayScreenTime: Int,
    ) : ScreenTimeConfigUiState
}