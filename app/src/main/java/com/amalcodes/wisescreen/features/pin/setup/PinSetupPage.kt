package com.amalcodes.wisescreen.features.pin.setup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun PinSetupPage(
    pinSetupUiState: PinSetupUiState,
    onSuccess: () -> Unit,
    savePin: (String) -> Unit,
    confirmPin: (String) -> Unit,
    clearErrorState: () -> Unit,
    resetPin: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val (pin, setPin) = remember { mutableStateOf("") }
    val inputPinTransition = updateTransition(targetState = pinSetupUiState, label = "pin_setup_transition")
    val inputPinBorderColor by inputPinTransition.animateColor(
        label = "pin_setup_border_color_animation",
        transitionSpec = { tween(300) },
        targetValueByState = { state ->
            if (state is PinSetupUiState.PinMismatch) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryContainer
        },
    )
    LaunchedEffect(key1 = pinSetupUiState) {
        when (pinSetupUiState) {
            is PinSetupUiState.Confirmed -> onSuccess()
            is PinSetupUiState.Initial -> setPin("")
            is PinSetupUiState.PinMismatch -> Unit
            is PinSetupUiState.NewPinCreated -> setPin("")
        }
    }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        awaitFrame()
        keyboardController?.show()
    }
    BackHandler(enabled = pinSetupUiState in listOf(PinSetupUiState.NewPinCreated, PinSetupUiState.PinMismatch)) {
        resetPin()
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(SpacingTokens.Space24))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when (pinSetupUiState) {
                is PinSetupUiState.Initial -> stringResource(id = R.string.text_Create_New_PIN)
                else -> stringResource(id = R.string.text_Confirm_PIN)
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space16))
        InputPinComponent(
            modifier = Modifier.focusRequester(focusRequester),
            value = pin,
            onValueChange = { newPin, filled ->
                setPin(newPin)
                when {
                    filled && pinSetupUiState is PinSetupUiState.NewPinCreated -> confirmPin(newPin)
                    pinSetupUiState is PinSetupUiState.PinMismatch -> clearErrorState()
                    filled -> savePin(newPin)
                }
            },
            borderColor = inputPinBorderColor,
        )
        AnimatedVisibility(visible = pinSetupUiState is PinSetupUiState.PinMismatch) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacingTokens.Space16),
                text = stringResource(id = R.string.text_PIN_Mismatch),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}