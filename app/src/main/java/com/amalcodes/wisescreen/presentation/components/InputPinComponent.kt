package com.amalcodes.wisescreen.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

private const val DEFAULT_PIN_COUNT = 6

@Composable
fun InputPinComponent(
    value: String,
    onValueChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    cellModifier: Modifier = Modifier,
    count: Int = DEFAULT_PIN_COUNT,
    visualTransformation: VisualTransformation = PasswordVisualTransformation(mask = '⬤'),
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange@{ newValue ->
            if (newValue.count() > count) return@onValueChange
            onValueChange.invoke(newValue, newValue.count() == count)
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = visualTransformation,
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(count) { index ->
                    PinCellComponent(
                        modifier = cellModifier.border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(8.dp)
                        ),
                        value = value.getOrNull(index)?.toString()?.let { text ->
                            visualTransformation.filter(AnnotatedString(text)).text.text
                        } ?: " ",
                    )
                    if (index >= count - 1) return@repeat
                    Spacer(modifier = Modifier.width(SpacingTokens.Space8))
                }
            }
        }
    )
}

@Composable
fun PinCellComponent(
    value: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier,
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
    }

}