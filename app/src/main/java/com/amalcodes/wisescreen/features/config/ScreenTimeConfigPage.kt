package com.amalcodes.wisescreen.features.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.components.KeyValueMenu
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

@ExperimentalMaterial3Api
@Composable
fun ScreenTimeConfigPage(
    screenTimeConfigUiState: ScreenTimeConfigUiState,
    openDayPicker: (
        title: String,
        key: String,
        activeDays: List<Int>,
        onSubmit: (key: String, activeDays: List<Int>) -> Unit,
    ) -> Unit,
    openTimePicker: (
        title: String,
        key: String,
        timeInMillis: Int,
        onSubmit: (key: String, timeInMillis: Int) -> Unit,
    ) -> Unit,
    updateWorkDays: (days: List<Int>) -> Unit,
    updateWorkDayScreenTime: (timeInMillis: Int) -> Unit,
    updateRestDays: (days: List<Int>) -> Unit,
    updateRestDayScreenTime: (timeInMillis: Int) -> Unit,
) {
    Scaffold() { paddingValues ->
        when (screenTimeConfigUiState) {
            is ScreenTimeConfigUiState.NotShown -> Unit
            is ScreenTimeConfigUiState.Success -> ScreenTimeConfigSuccessPage(
                screenTimeConfigUiState = screenTimeConfigUiState,
                openDayPicker = openDayPicker,
                openTimePicker = openTimePicker,
                updateWorkDays = updateWorkDays,
                updateWorkDayScreenTime = updateWorkDayScreenTime,
                updateRestDays = updateRestDays,
                updateRestDayScreenTime = updateRestDayScreenTime,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ScreenTimeConfigSuccessPage(
    screenTimeConfigUiState: ScreenTimeConfigUiState.Success,
    openDayPicker: (
        title: String,
        key: String,
        activeDays: List<Int>,
        onSubmit: (key: String, activeDays: List<Int>) -> Unit,
    ) -> Unit,
    openTimePicker: (
        title: String,
        key: String,
        timeInMillis: Int,
        onSubmit: (key: String, timeInMillis: Int) -> Unit,
    ) -> Unit,
    updateWorkDays: (days: List<Int>) -> Unit,
    updateWorkDayScreenTime: (timeInMillis: Int) -> Unit,
    updateRestDays: (days: List<Int>) -> Unit,
    updateRestDayScreenTime: (timeInMillis: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        SectionScreenTimeDaysConfig(
            modifier = Modifier.padding(SpacingTokens.Space16),
            title = stringResource(id = R.string.text_Work_Days),
            activeDays = screenTimeConfigUiState.formattedWorkDays,
            dailyScreenTime = screenTimeConfigUiState.formattedWorkDayScreenTime,
            onClickRepeat = {
                openDayPicker(
                    context.getString(R.string.text_Work_Days),
                    "WORK_DAYS",
                    screenTimeConfigUiState.workDays
                ) { key, activeDays ->
                    if (key != "WORK_DAYS") return@openDayPicker
                    updateWorkDays(activeDays)
                }
            },
            onClickDailyScreenTime = {
                openTimePicker(
                    context.getString(R.string.text_Work_Days),
                    "WORK_DAY_SCREEN_TIME",
                    screenTimeConfigUiState.workingDayScreenTime
                ) { key, timeInMillis ->
                    if (key != "WORK_DAY_SCREEN_TIME") return@openTimePicker
                    updateWorkDayScreenTime(timeInMillis)
                }
            }
        )
        Divider(thickness = SpacingTokens.Space8)
        SectionScreenTimeDaysConfig(
            modifier = Modifier.padding(SpacingTokens.Space16),
            title = stringResource(id = R.string.text_Rest_Days),
            activeDays = screenTimeConfigUiState.formattedRestDays,
            dailyScreenTime = screenTimeConfigUiState.formattedRestDayScreenTime,
            onClickRepeat = {
                openDayPicker(
                    context.getString(R.string.text_Rest_Days),
                    "REST_DAYS",
                    screenTimeConfigUiState.restDays
                ) { key, activeDays ->
                    if (key != "REST_DAYS") return@openDayPicker
                    updateRestDays(activeDays)
                }
            },
            onClickDailyScreenTime = {
                openTimePicker(
                    context.getString(R.string.text_Rest_Days),
                    "REST_DAY_SCREEN_TIME",
                    screenTimeConfigUiState.restDayScreenTime
                ) { key, timeInMillis ->
                    if (key != "REST_DAY_SCREEN_TIME") return@openTimePicker
                    updateRestDayScreenTime(timeInMillis)
                }
            }
        )
    }
}

@Composable
private fun SectionScreenTimeDaysConfig(
    title: String,
    activeDays: String,
    dailyScreenTime: String,
    modifier: Modifier = Modifier,
    onClickRepeat: () -> Unit,
    onClickDailyScreenTime: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space4))
        KeyValueMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpacingTokens.Space4)
                .clickable(onClick = onClickRepeat),
            keyValue = stringResource(id = R.string.text_Repeat) to activeDays,
        )
        KeyValueMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpacingTokens.Space4)
                .clickable(onClick = onClickDailyScreenTime),
            keyValue = stringResource(id = R.string.text_Daily_screen_time) to dailyScreenTime,
        )
    }
}