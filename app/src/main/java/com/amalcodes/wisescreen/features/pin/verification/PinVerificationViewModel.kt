package com.amalcodes.wisescreen.features.pin.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.domain.usecase.IsPinMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PinVerificationViewModel @Inject constructor(
    private val isPinMatchUseCase: IsPinMatchUseCase,
) : ViewModel() {

    private val _pinVerificationUiState: MutableStateFlow<PinVerificationUiState> = MutableStateFlow(PinVerificationUiState.Typing)
    val pinVerificationUiState: StateFlow<PinVerificationUiState> = _pinVerificationUiState

    fun verifyPin(pin: String) {
        isPinMatchUseCase.invoke(pin)
            .map { if (it) PinVerificationUiState.Correct else PinVerificationUiState.Incorrect }
            .onEach { newState -> _pinVerificationUiState.update { newState } }
            .launchIn(viewModelScope)
    }

    fun onType() {
        _pinVerificationUiState.update { last -> if (last is PinVerificationUiState.Incorrect) PinVerificationUiState.Typing else last }
    }

}