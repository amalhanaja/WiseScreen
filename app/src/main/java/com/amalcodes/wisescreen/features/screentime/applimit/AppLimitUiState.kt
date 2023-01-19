package com.amalcodes.wisescreen.features.screentime.applimit

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity

sealed interface AppLimitUiState {
    object Empty : AppLimitUiState
    data class WithData(val data: List<AppLimitEntity>) : AppLimitUiState
}