package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.domain.usecase.IsPinSetUseCase
import com.amalcodes.wisescreen.domain.usecase.UseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.HomeViewEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * @author: AMAL
 * Created On : 22/07/20
 */


@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    private val getUsageStatsUseCase: GetUsageStatsUseCase,
    private val isPinSetUseCase: IsPinSetUseCase
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
                HomeViewEntity(
                    dailyUsage = list.sortedByDescending { it.totalTimeInForeground }.take(3),
                    totalTimeInForeground = totalTimeInForeground,
                    isPinSet = false
                )
            }.zip(isPinSetUseCase(UseCase.None)) { t1, t2 ->
                HomeUIState.Content(t1.copy(isPinSet = t2)) as UIState
            }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}