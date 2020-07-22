package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.viewentity.ItemUsageViewEntity

/**
 * @author: AMAL
 * Created On : 18/07/20
 */
 
 
sealed class ScreenTimeUIState: UIState.Abstract() {
    data class Content(val usageItems: List<ItemUsageViewEntity>): ScreenTimeUIState()
}

sealed class ScreenTimeUIEvent: UIEvent.Abstract() {
    object Fetch: ScreenTimeUIEvent()
}