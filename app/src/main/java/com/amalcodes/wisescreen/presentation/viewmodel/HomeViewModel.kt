package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.core.zip3
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.*
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
    private val isPinSetUseCase: IsPinSetUseCase,
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase,
    private val updateScreenTimeConfigUseCase: UpdateScreenTimeConfigUseCase
) : BaseViewModel() {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is HomeUIEvent.Fetch -> fetch()
            is HomeUIEvent.UpdateScreenTimeConfig -> updateScreenTimeConfig(event.screenTimeConfigEntity)
            else -> event.unhandled()
        }
    }

    private fun updateScreenTimeConfig(config: ScreenTimeConfigEntity) {
        updateScreenTimeConfigUseCase(config)
            .flatMapLatest { getUsageStats() }
            .catch { emit(it.toUIState()) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }

    private fun getUsageStats() = getUsageStatsUseCase(TimeRangeEnum.TODAY)
        .zip3(
            getScreenTimeConfigUseCase(UseCase.None),
            isPinSetUseCase(UseCase.None)
        ) { t1, t2, t3 ->
            val totalTimeInForeground = t1.fold(0L) { acc, appUsageEntity ->
                acc + appUsageEntity.totalTimeInForeground
            }
            val viewEntity = HomeViewEntity(
                dailyUsage = t1.sortedByDescending { it.totalTimeInForeground }.take(3),
                totalTimeInForeground = totalTimeInForeground,
                isPinSet = t3,
                screenTimeConfigEntity = t2
            )
            HomeUIState.Content(viewEntity) as UIState
        }

    private fun fetch() {
        getUsageStats()
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}