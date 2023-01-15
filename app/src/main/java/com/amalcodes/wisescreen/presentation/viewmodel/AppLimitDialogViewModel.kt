package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.usecase.UpdateAppLimitUseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toAppLimitEntity
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.AppLimitDialogUIEvent
import com.amalcodes.wisescreen.presentation.ui.AppLimitDialogUIState
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 26/07/20
 */


@ExperimentalCoroutinesApi
@HiltViewModel
class AppLimitDialogViewModel @Inject constructor(
    private val updateUpdateAppLimitUseCase: UpdateAppLimitUseCase
) : BaseViewModel() {
    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is AppLimitDialogUIEvent.Update -> update(event.viewEntity)
            else -> event.unhandled()
        }
    }

    private fun update(viewEntity: AppLimitViewEntity) {
        updateUpdateAppLimitUseCase(viewEntity.toAppLimitEntity())
            .map { AppLimitDialogUIState.Updated as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}