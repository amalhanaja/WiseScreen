package com.amalcodes.wisescreen.features.pin.verification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.components.InputPinComponent
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import kotlinx.coroutines.android.awaitFrame

@ExperimentalComposeUiApi
@Composable
fun PinVerificationDialog(
    onVerify: (String) -> Unit,
    pinVerificationUiState: PinVerificationUiState,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    onVerified: () -> Unit,
    onType: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val (pin, setPin) = remember { mutableStateOf("") }
    val inputPinTransition = updateTransition(targetState = pinVerificationUiState, label = "input_pin_transition")
    val inputPinBorderColor by inputPinTransition.animateColor(
        label = "input_pin_border_color_animation",
        transitionSpec = { tween(300) },
        targetValueByState = { state ->
            if (state is PinVerificationUiState.Incorrect) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryContainer
        },
    )

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        awaitFrame()
        keyboardController?.show()
    }
    LaunchedEffect(pinVerificationUiState) {
        if (pinVerificationUiState != PinVerificationUiState.Correct) return@LaunchedEffect
        onVerified()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(SpacingTokens.Space16))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.text_Enter_PIN),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space24))
        InputPinComponent(
            value = pin,
            onValueChange = { pin, filled ->
                if (filled) onVerify(pin) else onType()
                setPin(pin)
            },
            modifier = Modifier.focusRequester(focusRequester),
            borderColor = inputPinBorderColor,
        )
        AnimatedVisibility(visible = pinVerificationUiState is PinVerificationUiState.Incorrect) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacingTokens.Space16),
                text = stringResource(id = R.string.text_Incorrect_PIN),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(SpacingTokens.Space16))
        TextButton(onClick = onCancel) {
            Text(text = stringResource(id = R.string.text_Cancel))
        }
        Spacer(modifier = Modifier.height(SpacingTokens.Space8))
    }
}