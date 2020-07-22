package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.HomeViewEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

/**
 * @author: AMAL
 * Created On : 22/07/20
 */


@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    private val getUsageStatsUseCase: GetUsageStatsUseCase
) : BaseViewModel() {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            HomeUIEvent.Fetch -> fetch()
            else -> event.unhandled()
        }
    }

    private fun fetch() {
        getUsageStatsUseCase(TimeRangeEnum.TODAY)
            .map { list ->
                val totalTimeInForeground = list.fold(0L) { acc, appUsageEntity ->
                    acc + appUsageEntity.totalTimeInForeground
                }
                HomeUIState.Content(
                    HomeViewEntity(
                        dailyUsage = list.sortedByDescending { it.totalTimeInForeground }.take(3),
                        totalTimeInForeground = totalTimeInForeground
                    )
                ) as UIState
            }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}