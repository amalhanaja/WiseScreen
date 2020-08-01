package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState

/**
 * @author: AMAL
 * Created On : 01/08/20
 */


sealed class PinDialogUIState : UIState.Abstract() {
    object PinCorrect : PinDialogUIState()
    object IncorrectPin : PinDialogUIState()
}

sealed class PinDialogUIEvent : UIEvent.Abstract() {
    data class SubmitPin(val pin: String) : PinDialogUIEvent()
}