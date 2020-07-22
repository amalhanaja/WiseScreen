package com.amalcodes.wisescreen.presentation.ui

import com.amalcodes.wisescreen.presentation.UIEvent
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.viewentity.HomeViewEntity

/**
 * @author: AMAL
 * Created On : 22/07/20
 */
 
 
sealed class HomeUIState: UIState.Abstract() {
    class Content(val viewEntity: HomeViewEntity): HomeUIState()
}

sealed class HomeUIEvent: UIEvent.Abstract() {
    object Fetch: HomeUIEvent()
}