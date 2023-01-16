package com.amalcodes.wisescreen.features.home

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.components.StackedBarChartWithLegendComponent
import com.amalcodes.wisescreen.presentation.components.SwitchText
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun HomePage(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState,
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
            )
            Divider(thickness = SpacingTokens.Space8)
            SectionScreenTimeManagement(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Space16),
                checked = true,
                onCheckedChange = { }
            )
            Divider(thickness = SpacingTokens.Space8)
            SectionPin(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Space16),
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}

@Composable
private fun SectionScreenTime(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState,
    modifier: Modifier = Modifier,
) {
    when (sectionScreenTimeSummaryUiState) {
        is SectionScreenTimeSummaryUiState.Loading -> SectionScreenTimeLoading(
            sectionScreenTimeSummaryUiState = sectionScreenTimeSummaryUiState,
            modifier = modifier
        )
        is SectionScreenTimeSummaryUiState.Success -> SectionScreenTimeSuccess(
            sectionScreenTimeSummaryUiState = sectionScreenTimeSummaryUiState,
            modifier = modifier
        )
    }
}

@Composable
private fun SectionScreenTimeSuccess(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState.Success,
    modifier: Modifier = Modifier,
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
            onClick = { /*TODO*/ },
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
private fun SectionScreenTimeLoading(
    sectionScreenTimeSummaryUiState: SectionScreenTimeSummaryUiState.Loading,
    modifier: Modifier = Modifier,
) {
    Text(text = "Loading", modifier = modifier)
}

@Composable
private fun SectionScreenTimeManagement(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(modifier = modifier) {
        SwitchText(text = stringResource(id = R.string.text_Screen_time_management), checked = checked, onCheckedChange = onCheckedChange)
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