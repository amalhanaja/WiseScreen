package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.core.zip3
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.DisablePinUseCase
import com.amalcodes.wisescreen.domain.usecase.GetScreenTimeConfigUseCase
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.domain.usecase.IsPinSetUseCase
import com.amalcodes.wisescreen.domain.usecase.UpdateScreenTimeConfigUseCase
import com.amalcodes.wisescreen.domain.usecase.UseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.HomeViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 22/07/20
 */


@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsageStatsUseCase: GetUsageStatsUseCase,
    private val isPinSetUseCase: IsPinSetUseCase,
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase,
    private val updateScreenTimeConfigUseCase: UpdateScreenTimeConfigUseCase,
    private val disablePinUseCase: DisablePinUseCase
) : BaseViewModel() {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is HomeUIEvent.Fetch -> fetch()
            is HomeUIEvent.DisablePIN -> disablePIN()
            is HomeUIEvent.UpdateScreenTimeConfig -> updateScreenTimeConfig(event.screenTimeConfigEntity)
            else -> event.unhandled()
        }
    }

    private fun disablePIN() {
        disablePinUseCase(UseCase.None)
            .flatMapLatest { getUsageStats() }
            .catch { emit(it.toUIState()) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
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