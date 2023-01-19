package com.amalcodes.wisescreen.presentation.components

import android.view.ContextThemeWrapper
import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.amalcodes.wisescreen.core.millis

@Composable
fun TimePickerComponent(
    millis: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TimePicker(ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_DarkActionBar)).apply {
                this.setIs24HourView(true)
                setOnTimeChangedListener { view, _, _ -> onChange(view.millis) }
            }
        },
        update = { timePiker -> timePiker.millis = millis }
    )
}