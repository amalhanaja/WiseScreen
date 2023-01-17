package com.amalcodes.wisescreen.features.home

import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendComponentState

sealed interface SectionScreenTimeSummaryUiState {
    data class Success(val totalUsage: String, val chartData: StackedBarChartWithLegendComponentState) : SectionScreenTimeSummaryUiState
    object NotShown : SectionScreenTimeSummaryUiState
}