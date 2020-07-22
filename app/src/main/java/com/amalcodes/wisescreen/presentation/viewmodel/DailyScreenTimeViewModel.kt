package com.amalcodes.wisescreen.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import com.amalcodes.wisescreen.core.BaseViewModel
import com.amalcodes.wisescreen.presentation.UIEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DailyScreenTimeViewModel @ViewModelInject constructor(): BaseViewModel() {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            else -> event.unhandled()
        }
    }
}
