package com.amalcodes.wisescreen.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amalcodes.wisescreen.core.clearTime
import java.util.Calendar
import java.util.Locale


private const val DAYS_COUNT = 7

@Composable
fun DayPickerComponent(
    selected: List<Int>,
    onSelectChange: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(DAYS_COUNT) { index ->
            val day = index + 1
            DayPickerItem(
                dayOfWeek = day,
                checked = day in selected,
                onCheckedChange = { onSelectChange(if (it) selected + day else selected - day) }
            )
        }
    }
}

@Composable
private fun DayPickerItem(
    dayOfWeek: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cal = Calendar.getInstance().apply {
        clearTime()
        this[Calendar.DAY_OF_WEEK] = dayOfWeek
    }
    val dayOfWeekName = cal.getDisplayName(
        Calendar.DAY_OF_WEEK,
        Calendar.LONG,
        Locale.getDefault()
    ).orEmpty()

    Row(
        modifier = modifier.fillMaxWidth().height(36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = dayOfWeekName,
            style = MaterialTheme.typography.bodyMedium
        )
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}