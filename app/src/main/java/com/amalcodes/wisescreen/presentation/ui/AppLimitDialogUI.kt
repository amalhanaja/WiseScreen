package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity

/**
 * @author: AMAL
 * Created On : 26/07/20
 */


sealed class AppLimitDialogUIState: UIState.Abstract() {
    object Updated: AppLimitDialogUIState()
}

sealed class AppLimitDialogUIEvent: UIEvent.Abstract() {
    data class Update(val viewEntity: AppLimitViewEntity): AppLimitDialogUIEvent()
}