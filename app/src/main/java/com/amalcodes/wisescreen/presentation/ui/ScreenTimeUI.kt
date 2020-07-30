package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.viewentity.UsageItemViewEntity

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


sealed class ScreenTimeUIState : UIState.Abstract() {
    data class Content(val usageItems: List<UsageItemViewEntity>) : ScreenTimeUIState()
}

sealed class ScreenTimeUIEvent : UIEvent.Abstract() {
    data class Fetch(val timeRangeEnum: TimeRangeEnum) : ScreenTimeUIEvent()
}