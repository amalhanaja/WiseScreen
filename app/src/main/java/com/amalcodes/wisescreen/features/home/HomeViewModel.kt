package com.amalcodes.wisescreen.features.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.DateTimeFormatter
import com.amalcodes.wisescreen.core.ResourceGetter
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendComponentState
import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendItem
import com.amalcodes.wisescreen.presentation.foundation.ColorPalettes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class HomeViewModel @Inject constructor(
    getUsageStatsUseCase: GetUsageStatsUseCase,
    dateTimeFormatter: DateTimeFormatter,
    resourceGetter: ResourceGetter,
) : ViewModel() {

    private companion object {
        const val TAKE_TOP_3 = 3
    }

    val sectionScreenTimeSummaryUiState: StateFlow<SectionScreenTimeSummaryUiState> = getUsageStatsUseCase(TimeRangeEnum.TODAY)
        .map { usages ->
            val totalUsage = usages.sumOf { it.totalTimeInForeground }.takeIf { it > 0 } ?: 1L
            val chartColors = arrayOf(ColorPalettes.PersianGreen, ColorPalettes.OrangeYellowCrayola, ColorPalettes.BurntSienna, Color.DarkGray)
            val mostUsed = usages.sortedByDescending { it.totalTimeInForeground }.take(TAKE_TOP_3)
            val others = AppUsageEntity(
                packageName = "",
                appName = resourceGetter.getString(R.string.text_Others),
                appIcon = null,
                isSystemApp = false,
                totalTimeInForeground = totalUsage - mostUsed.sumOf { it.totalTimeInForeground }
            ).takeIf { it.totalTimeInForeground > 0 }?.let { listOf(it) }.orEmpty()
            SectionScreenTimeSummaryUiState.Success(
                totalUsage = dateTimeFormatter.formatTimeInMillis(millis = totalUsage),
                chartData = StackedBarChartWithLegendComponentState(
                    data = mostUsed.plus(elements = others).mapIndexed { index, usage ->
                        StackedBarChartWithLegendItem(
                            percentage = usage.totalTimeInForeground / totalUsage.toFloat(),
                            color = chartColors[index],
                            label = usage.appName,
                            description = dateTimeFormatter.formatTimeInMillis(usage.totalTimeInForeground)
                        )
                    }.filter { it.percentage > 0f }
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SectionScreenTimeSummaryUiState.Loading
        )
}