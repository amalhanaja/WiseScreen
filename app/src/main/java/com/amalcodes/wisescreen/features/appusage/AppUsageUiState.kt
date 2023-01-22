package com.amalcodes.wisescreen.features.appusage

sealed interface AppUsageUiState {
    data class Success(val data: List<Data>) : AppUsageUiState

    object Loading : AppUsageUiState

    data class Data(
        val totalUsage: String,
        val start: String,
        val end: String,
        val percentage: Float,
        val usages: List<AppUsageListItem>,
    )
}