package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toItemUsageViewEntity
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.ScreenTimeUIEvent
import com.amalcodes.wisescreen.presentation.ui.ScreenTimeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val getUsageStatsUseCase: GetUsageStatsUseCase
) : BaseViewModel() {
    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is ScreenTimeUIEvent.Fetch -> fetch(event.timeRangeEnum)
            else -> event.unhandled()
        }
    }


    private fun fetch(timeRangeEnum: TimeRangeEnum) {
        getUsageStatsUseCase(timeRangeEnum)
            .map { list ->
                list.map {
                    it.toItemUsageViewEntity(
                        totalUsage = list.sumBy { item -> item.totalTimeInForeground.toInt() }
                    )
                }
            }
            .map { ScreenTimeUIState.Content(usageItems = it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}