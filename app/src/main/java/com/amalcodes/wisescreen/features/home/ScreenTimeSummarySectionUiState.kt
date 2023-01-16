package com.amalcodes.wisescreen.features.home

import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendComponentState
import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendItem

sealed interface ScreenTimeSummarySectionUiState {
    data class Success(val totalUsage: String, val chartData: StackedBarChartWithLegendComponentState) : ScreenTimeSummarySectionUiState
    object Loading : ScreenTimeSummarySectionUiState
}