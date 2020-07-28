package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.usecase.SetPinUseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIEvent
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIFailure
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


@ExperimentalCoroutinesApi
class PinSetupViewModel @ViewModelInject constructor(
    private val setPinUseCase: SetPinUseCase
) : BaseViewModel() {

    private var _pin: String = ""

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is PinSetupUIEvent.SetNewPin -> setNewPin(event.pin)
            is PinSetupUIEvent.VerifyPin -> verifyPin(event.pin)
            else -> event.unhandled()
        }
    }

    private fun verifyPin(pin: String) {
        if (_pin != pin) {
            _uiState.postValue(PinSetupUIFailure.PinMismatch)
        } else {
            setPinUseCase(pin)
                .map { PinSetupUIState.PinVerified as UIState }
                .catch { emit(it.toUIState()) }
                .onEach { _uiState.postValue(it) }
                .launchIn(viewModelScope)
        }
    }

    private fun setNewPin(pin: String) {
        _pin = pin
        _uiState.postValue(PinSetupUIState.NewPinCreated)
    }
}