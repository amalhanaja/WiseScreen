package com.amalcodes.wisescreen.features.pin.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.domain.usecase.SetPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PinSetupViewModel @Inject constructor(
    private val setPinUseCase: SetPinUseCase,
) : ViewModel() {

    private var _pin: String = ""
    private val _pinSetupUiState: MutableStateFlow<PinSetupUiState> = MutableStateFlow(PinSetupUiState.Initial)
    val pinSetupUiState: StateFlow<PinSetupUiState> = _pinSetupUiState

    fun savePin(pin: String) {
        _pin = pin
        _pinSetupUiState.update { PinSetupUiState.NewPinCreated }
    }

    fun confirmPin(pin: String) {
        if (_pin != pin) {
            _pinSetupUiState.update { PinSetupUiState.PinMismatch }
            return
        }
        setPinUseCase(pin)
            .map { PinSetupUiState.Confirmed }
            .onEach { _pinSetupUiState.update { PinSetupUiState.Confirmed } }
            .launchIn(viewModelScope)
    }

    fun clearErrorState() {
        _pinSetupUiState.update { prev -> if (prev is PinSetupUiState.PinMismatch) PinSetupUiState.NewPinCreated else prev }
    }

    fun resetPin() {
        _pin = ""
        _pinSetupUiState.update { PinSetupUiState.Initial }
    }
}