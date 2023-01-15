package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.usecase.GetScreenTimeConfigUseCase
import com.amalcodes.wisescreen.domain.usecase.UpdateScreenTimeConfigUseCase
import com.amalcodes.wisescreen.domain.usecase.UseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.DailyScreenTimeUIEvent
import com.amalcodes.wisescreen.presentation.ui.DailyScreenTimeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DailyScreenTimeViewModel @Inject constructor(
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase,
    private val updateScreenTimeConfigUseCase: UpdateScreenTimeConfigUseCase
) : BaseViewModel() {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is DailyScreenTimeUIEvent.Fetch -> fetch()
            is DailyScreenTimeUIEvent.UpdateScreenTimeConfig -> updateScreenTimeConfig(event.data)
            else -> event.unhandled()
        }
    }

    private fun updateScreenTimeConfig(data: ScreenTimeConfigEntity) {
        Timber.d("updateScreenTime: $data")
        updateScreenTimeConfigUseCase(data)
            .map { DailyScreenTimeUIState.Content(data) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }

    private fun fetch() {
        getScreenTimeConfigUseCase(UseCase.None)
            .map { DailyScreenTimeUIState.Content(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}
