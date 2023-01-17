package com.amalcodes.wisescreen.features.home

interface SectionConfigUiState {
    data class Success(val isPinEnabled: Boolean, val isScreenTimeManageable: Boolean) : SectionConfigUiState
    object NotShown : SectionConfigUiState
}