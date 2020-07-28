package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


sealed class PinSetupUIState : UIState.Abstract() {
    object Completed : PinSetupUIState()
    object NewPinCreated : PinSetupUIState()
    object PinVerified : PinSetupUIState()
}

sealed class PinSetupUIFailure(message: String?, cause: Throwable? = null) :
    UIState.UIFailure.Abstract(
        message,
        cause
    ) {
    object PinMismatch: PinSetupUIFailure("pin mismatch")
}

sealed class PinSetupUIEvent : UIEvent.Abstract() {
    data class SetNewPin(val pin: String) : PinSetupUIEvent()
    data class VerifyPin(val pin: String) : PinSetupUIEvent()
    data class SecurityQuestion(val question: String, val answer: String) : PinSetupUIEvent()
}