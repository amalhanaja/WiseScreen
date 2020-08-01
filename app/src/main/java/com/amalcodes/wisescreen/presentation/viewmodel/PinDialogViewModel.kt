package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.usecase.IsPinMatchUseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.PinDialogUIEvent
import com.amalcodes.wisescreen.presentation.ui.PinDialogUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * @author: AMAL
 * Created On : 01/08/20
 */


@ExperimentalCoroutinesApi
class PinDialogViewModel @ViewModelInject constructor(
    private val isPinMatchUseCase: IsPinMatchUseCase
) : BaseViewModel() {
    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is PinDialogUIEvent.SubmitPin -> submitPin(event.pin)
            else -> event.unhandled()
        }
    }

    private fun submitPin(pin: String) {
        isPinMatchUseCase(pin)
            .map {
                if (it) {
                    PinDialogUIState.PinCorrect
                } else {
                    PinDialogUIState.IncorrectPin
                } as UIState
            }
            .catch { emit(it.toUIState()) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}