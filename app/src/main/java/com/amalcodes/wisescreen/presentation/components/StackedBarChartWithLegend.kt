package com.amalcodes.wisescreen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

@Immutable
data class StackedBarChartWithLegendComponentState(
    val data: List<StackedBarChartWithLegendItem>,
)

@Immutable
data class StackedBarChartWithLegendItem(
    val percentage: Float,
    val color: Color,
    val label: String,
    val description: String,
)

@Composable
fun StackedBarChartWithLegendComponent(
    modifier: Modifier,
    state: StackedBarChartWithLegendComponentState,
) {
    Column(
        modifier = modifier,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            state.data.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(item.percentage)
                        .height(8.dp)
                        .background(color = item.color, shape = RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(SpacingTokens.Space8))
        Row(modifier = Modifier.fillMaxWidth()) {
            state.data.forEach { item ->
                StackedBarChartLegend(item = item, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StackedBarChartLegend(
    item: StackedBarChartWithLegendItem,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (labelRef, indicatorRef, descriptionRef) = createRefs()
        Box(modifier = Modifier
            .constrainAs(ref = indicatorRef) {
                width = Dimension.value(8.dp)
                height = Dimension.value(8.dp)
                start.linkTo(parent.start)
                top.linkTo(labelRef.top)
                centerVerticallyTo(labelRef)
            }
            .background(color = item.color, shape = CircleShape)
        )
        Text(
            modifier = Modifier.constrainAs(ref = labelRef) {
                width = Dimension.fillToConstraints
                start.linkTo(indicatorRef.end, SpacingTokens.Space8)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            text = item.label,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            modifier = Modifier.constrainAs(ref = descriptionRef) {
                width = Dimension.fillToConstraints
                start.linkTo(labelRef.start)
                top.linkTo(labelRef.bottom)
                end.linkTo(labelRef.end)
            },
            text = item.description,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}