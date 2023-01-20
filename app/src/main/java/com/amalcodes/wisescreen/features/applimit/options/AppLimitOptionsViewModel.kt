package com.amalcodes.wisescreen.features.applimit.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.usecase.UpdateAppLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLimitOptionsViewModel @Inject constructor(
    private val updateAppLimitUseCase: UpdateAppLimitUseCase,
) : ViewModel() {
    fun updateAppLimit(appLimitEntity: AppLimitEntity) {
        viewModelScope.launch { updateAppLimitUseCase(appLimitEntity) }
    }
}