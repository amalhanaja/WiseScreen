package com.amalcodes.wisescreen.features.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.components.TimePickerComponent
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

@Composable
fun TimePickerDialog(
    title: String,
    millis: Int,
    onSubmit: (Int) -> Unit,
    onCancel: () -> Unit,
) {
    val (timeInMillis, setTimeInMillis) = remember { mutableStateOf(millis) }
    Column(modifier = Modifier.padding(vertical = SpacingTokens.Space16)) {
        Text(
            modifier = Modifier.padding(horizontal = SpacingTokens.Space16),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space8))
        TimePickerComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SpacingTokens.Space16),
            millis = timeInMillis,
            onChange = setTimeInMillis,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Space16)
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(id = R.string.text_Cancel))
            }
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { onSubmit(timeInMillis) }
            ) {
                Text(text = stringResource(id = R.string.text_OK))
            }
        }
    }
}