package com.amalcodes.wisescreen.features.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.DateTimeFormatter
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.usecase.GetScreenTimeConfigUseCase
import com.amalcodes.wisescreen.domain.usecase.UpdateScreenTimeConfigSuspendingUseCase
import com.amalcodes.wisescreen.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeConfigViewModel @Inject constructor(
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase,
    private val updateScreenTimeConfigSuspendingUseCase: UpdateScreenTimeConfigSuspendingUseCase,
    dateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    val screenTimeConfigUiState: StateFlow<ScreenTimeConfigUiState> = getScreenTimeConfigUseCase.invoke(UseCase.None)
        .map { entity ->
            val restDays = Util.getDaysOfWeek().filterNot { it in entity.workingDays }
            ScreenTimeConfigUiState.Success(
                formattedWorkDays = dateTimeFormatter.formatDaysOfWeek(entity.workingDays),
                formattedWorkDayScreenTime = dateTimeFormatter.formatTimeInMillis(entity.workingDayDailyScreenTimeInMillis.toLong()),
                formattedRestDays = dateTimeFormatter.formatDaysOfWeek(restDays),
                formattedRestDayScreenTime = dateTimeFormatter.formatTimeInMillis(entity.restDayDailyScreenTimeInMillis.toLong()),

                workDays = entity.workingDays,
                workingDayScreenTime = entity.workingDayDailyScreenTimeInMillis,
                restDays = restDays,
                restDayScreenTime = entity.restDayDailyScreenTimeInMillis,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ScreenTimeConfigUiState.NotShown
        )

    private suspend fun updateSavedConfig(updater: (ScreenTimeConfigEntity) -> ScreenTimeConfigEntity) {
        val lastConfig = getScreenTimeConfigUseCase.invoke(UseCase.None).first()
        updateScreenTimeConfigSuspendingUseCase(lastConfig.let(updater))
    }

    fun updateWorkDays(days: List<Int>) {
        viewModelScope.launch {
            updateSavedConfig { it.copy(workingDays = days) }
        }
    }

    fun updateWorkDayScreenTime(timeInMillis: Int) {
        viewModelScope.launch {
            updateSavedConfig { it.copy(workingDayDailyScreenTimeInMillis = timeInMillis) }
        }
    }

    fun updateRestDays(days: List<Int>) {
        viewModelScope.launch {
            updateSavedConfig { it.copy(workingDays = Util.getDaysOfWeek() - days.toSet()) }
        }
    }

    fun updateRestDayScreenTime(timeInMillis: Int) {
        viewModelScope.launch {
            updateSavedConfig { it.copy(restDayDailyScreenTimeInMillis = timeInMillis) }
        }
    }
}