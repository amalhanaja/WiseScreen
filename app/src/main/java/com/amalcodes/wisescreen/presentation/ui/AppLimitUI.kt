package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


sealed class AppLimitUIEvent : UIEvent.Abstract() {
    object Fetch : AppLimitUIEvent()
}

sealed class AppLimitUIState : UIState.Abstract() {
    data class Content(val apps: List<AppLimitEntity>) : AppLimitUIState()
}

class AppBlockedUIFailure(cause: Throwable) : UIState.UIFailure.Abstract("App Blocked", cause)