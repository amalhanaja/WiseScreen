package com.amalcodes.wisescreen.features.pin.setup

sealed interface PinSetupUiState {
    object NewPinCreated : PinSetupUiState
    object Confirmed : PinSetupUiState
    object PinMismatch : PinSetupUiState
    object Initial : PinSetupUiState
}