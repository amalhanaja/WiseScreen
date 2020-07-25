package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.domain.usecase.GetApplicationList
import com.amalcodes.wisescreen.domain.usecase.UseCase
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.toUIState
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIEvent
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
class AppLimitViewModel @ViewModelInject constructor(
    private val getApplicationList: GetApplicationList
) : BaseViewModel() {
    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is AppLimitUIEvent.Fetch -> fetch()
            else -> event.unhandled()
        }
    }

    private fun fetch() {
        getApplicationList(UseCase.None)
            .map { AppLimitUIState.Content(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}