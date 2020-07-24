package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


sealed class DailyScreenTimeUIEvent : UIEvent.Abstract() {
    object Fetch : DailyScreenTimeUIEvent()
    data class UpdateScreenTimeConfig(val data: ScreenTimeConfigEntity): DailyScreenTimeUIEvent()
}

sealed class DailyScreenTimeUIState : UIState.Abstract() {
    data class Content(val data: ScreenTimeConfigEntity) : DailyScreenTimeUIState()
}