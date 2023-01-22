package com.amalcodes.wisescreen.features.home

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendComponent
import com.amalcodes.wisescreen.presentation.components.SwitchText
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun HomePage(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState,
    sectionConfigUiState: SectionConfigUiState,
    toggleScreenTimeManageable: () -> Unit,
    togglePin: () -> Unit,
    goToPinVerification: (key: String, onSuccess: (key: String) -> Unit) -> Unit,
    goToCreatePin: () -> Unit,
    goToScreenTimeConfig: () -> Unit,
    goToAppLimitConfig: () -> Unit,
    goToDetailScreenTime: () -> Unit,
    notificationPermissionState: PermissionState? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    },
    goToAppNotificationSetting: () -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SectionScreenTime(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Space16),
                sectionScreenTimeSummaryUiState = sectionScreenTimeSummaryUiState,
                onClickMore = goToDetailScreenTime,
            )
            when (sectionConfigUiState) {
                is SectionConfigUiState.NotShown -> Unit
                is SectionConfigUiState.Success -> {
                    Divider(thickness = SpacingTokens.Space8)
                    SectionScreenTimeManagement(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Space16),
                        checked = sectionConfigUiState.isScreenTimeManageable,
                        onToggle = onToggle@{
                            if (sectionConfigUiState.isPinEnabled.not()) {
                                toggleScreenTimeManageable()
                                return@onToggle
                            }
                            goToPinVerification("CHANGE_IS_SCREEN_MANAGEABLE") { key ->
                                if (key != "CHANGE_IS_SCREEN_MANAGEABLE") return@goToPinVerification
                                toggleScreenTimeManageable()
                            }
                        }
                    )
                    AnimatedVisibility(visible = sectionConfigUiState.isScreenTimeManageable) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SpacingTokens.Space8)
                        ) {
                            MenuItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (sectionConfigUiState.isPinEnabled.not()) {
                                            goToScreenTimeConfig()
                                            return@clickable
                                        }
                                        goToPinVerification("SCREEN_TIME_CONFIG") { key ->
                                            if (key != "SCREEN_TIME_CONFIG") return@goToPinVerification
                                            goToScreenTimeConfig()
                                        }
                                    },
                                imagePainter = painterResource(id = R.drawable.ic_hourglass),
                                title = stringResource(id = R.string.text_Screen_Time),
                                description = stringResource(id = R.string.text_Screen_time_menu_description),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = SpacingTokens.Space48)
                            ) {
                                Divider()
                            }
                            MenuItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (sectionConfigUiState.isPinEnabled.not()) {
                                            goToAppLimitConfig()
                                            return@clickable
                                        }
                                        goToPinVerification("APP_LIMIT_CONFIG") { key ->
                                            if (key != "APP_LIMIT_CONFIG") return@goToPinVerification
                                            goToAppLimitConfig()
                                        }
                                    },
                                imagePainter = painterResource(id = R.drawable.ic_warning_o),
                                title = stringResource(id = R.string.text_App_limits),
                                description = stringResource(id = R.string.text_App_limits_description),
                            )
                            Spacer(modifier = Modifier.height(SpacingTokens.Space8))
                        }
                    }
                    Divider(thickness = SpacingTokens.Space8)
                    SectionPin(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Space16),
                        checked = sectionConfigUiState.isPinEnabled,
                        onCheckedChange = onCheckedChange@{ checked ->
                            if (checked) {
                                goToCreatePin()
                                return@onCheckedChange
                            }
                            goToPinVerification("DISABLE_PIN") { key ->
                                if (key != "DISABLE_PIN") return@goToPinVerification
                                togglePin()
                            }
                        },
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Divider(thickness = SpacingTokens.Space8)
                        SwitchText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(SpacingTokens.Space16),
                            text = stringResource(R.string.text_Notification),
                            checked = notificationPermissionState?.status == PermissionStatus.Granted,
                            onCheckedChange = onCheckedChange@{
                                if (!it) {
                                    goToAppNotificationSetting()
                                    return@onCheckedChange
                                }
                                notificationPermissionState?.launchPermissionRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionScreenTime(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState,
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit,
) {
    when (sectionScreenTimeSummaryUiState) {
        is SectionScreenTimeSummaryUiState.NotShown -> Unit
        is SectionScreenTimeSummaryUiState.Success -> SectionScreenTimeSuccess(
            modifier = modifier,
            sectionScreenTimeSummaryUiState = sectionScreenTimeSummaryUiState,
            onClickMore = onClickMore,
        )
    }
}

@Composable
private fun SectionScreenTimeSuccess(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState.Success,
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (labelValueRef, btnMoreRef, chartRef) = createRefs()
        Column(
            modifier = Modifier.constrainAs(ref = labelValueRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(btnMoreRef.start)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = stringResource(id = R.string.text_Screen_Time), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(SpacingTokens.Space4))
            Text(text = sectionScreenTimeSummaryUiState.totalUsage, style = MaterialTheme.typography.bodyLarge)
        }
        TextButton(
            modifier = Modifier.constrainAs(ref = btnMoreRef) {
                start.linkTo(labelValueRef.end)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            onClick = onClickMore,
        ) {
            Text(text = stringResource(id = R.string.text_More))
        }
        StackedBarChartWithLegendComponent(
            modifier = Modifier.constrainAs(ref = chartRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(labelValueRef.bottom, SpacingTokens.Space8)
                width = Dimension.fillToConstraints
            },
            state = sectionScreenTimeSummaryUiState.chartData
        )
    }
}

@Composable
private fun SectionScreenTimeManagement(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Column(modifier = modifier) {
        SwitchText(
            text = stringResource(id = R.string.text_Screen_time_management),
            checked = checked,
            onCheckedChange = { onToggle() },
        )
    }
}

@Composable
private fun SectionPin(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SwitchText(
        modifier = modifier,
        text = stringResource(id = R.string.text_wise_screen_pin),
        checked = checked,
        onCheckedChange = onCheckedChange,
    )
}

@Composable
private fun MenuItem(
    imagePainter: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (imageRef, titleRef, titleDescSpacerRef, descriptionRef) = createRefs()
        Image(
            modifier = Modifier.constrainAs(ref = imageRef) {
                start.linkTo(parent.start, margin = SpacingTokens.Space8)
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    topMargin = SpacingTokens.Space16,
                    bottomMargin = SpacingTokens.Space16
                )
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
            },
            painter = imagePainter,
            contentDescription = title,
            colorFilter = ColorFilter.tint(color = LocalContentColor.current)
        )
        Text(
            modifier = Modifier.constrainAs(ref = titleRef) {
                start.linkTo(imageRef.end, SpacingTokens.Space16)
                end.linkTo(parent.end, SpacingTokens.Space8)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            },
            text = title,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.constrainAs(ref = titleDescSpacerRef) {
            width = Dimension.fillToConstraints
            height = Dimension.value(SpacingTokens.Space4)
        })
        Text(
            modifier = Modifier.constrainAs(ref = descriptionRef) {
                start.linkTo(imageRef.end, SpacingTokens.Space16)
                end.linkTo(parent.end, SpacingTokens.Space8)
                width = Dimension.fillToConstraints
            },
            text = description,
            style = MaterialTheme.typography.bodySmall,
        )
        createVerticalChain(titleRef, titleDescSpacerRef, descriptionRef, chainStyle = ChainStyle.Packed)
    }
}