package com.amalcodes.wisescreen.features.pin.verification

sealed interface PinVerificationUiState {
    object Correct : PinVerificationUiState
    object Incorrect : PinVerificationUiState
    object Typing : PinVerificationUiState
}