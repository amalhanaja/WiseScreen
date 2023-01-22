package com.amalcodes.wisescreen.features.appusage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.ApplicationInfoProvider
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.DateTimeFormatter
import com.amalcodes.wisescreen.core.ResourceGetter
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AppUsageViewModel @Inject constructor(
    getUsageStatsUseCase: GetUsageStatsUseCase,
    dateTimeFormatter: DateTimeFormatter,
    resourceGetter: ResourceGetter,
    applicationInfoProvider: ApplicationInfoProvider,
) : ViewModel() {

    val appUsageUiState: StateFlow<AppUsageUiState> =
        combine(
            getUsageStatsUseCase(TimeRangeEnum.TODAY),
            getUsageStatsUseCase(TimeRangeEnum.THIS_WEEK)
        ) { todayUsages, thisWeekUsages ->
            val data = listOf(todayUsages, thisWeekUsages).mapIndexed { index, usages ->
                val timeRange = TimeRangeEnum.values()[index]
                val total = usages.sumOf { it.totalTimeInForeground }
                val max = when (timeRange) {
                    TimeRangeEnum.TODAY -> Const.ONE_DAY
                    TimeRangeEnum.THIS_WEEK -> Const.ONE_WEEK
                }
                AppUsageUiState.Data(
                    totalUsage = dateTimeFormatter.formatTimeInMillis(total),
                    start = when (timeRange) {
                        TimeRangeEnum.TODAY -> resourceGetter.getString(R.string.text_number_0)
                        TimeRangeEnum.THIS_WEEK -> resourceGetter.getString(R.string.Six_days_ago)
                    },
                    end = when (timeRange) {
                        TimeRangeEnum.TODAY -> resourceGetter.getString(R.string.text_number_24)
                        TimeRangeEnum.THIS_WEEK -> resourceGetter.getString(R.string.text_Today)
                    },
                    percentage = total.toFloat() / max,
                    usages = usages.map { entity ->
                        AppUsageListItem(
                            appName = entity.appName,
                            icon = applicationInfoProvider.getApplicationIcon(entity.packageName),
                            progress = entity.totalTimeInForeground.toFloat() / max,
                            totalTimeUsage = dateTimeFormatter.formatTimeInMillis(entity.totalTimeInForeground)
                        )
                    },
                )
            }
            AppUsageUiState.Success(data)
        }.flowOn(Dispatchers.IO).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppUsageUiState.Loading
        )

}