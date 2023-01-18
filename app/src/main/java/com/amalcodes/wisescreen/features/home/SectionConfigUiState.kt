package com.amalcodes.wisescreen.features.home

sealed interface SectionConfigUiState {
    data class Success(val isPinEnabled: Boolean, val isScreenTimeManageable: Boolean) : SectionConfigUiState
    object NotShown : SectionConfigUiState
}