package com.amalcodes.wisescreen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

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
                        .background(color = item.color, shape = RoundedCornerShape(8.dp)))
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            state.data.forEach { item ->
                StackedBarChartLegend(item = item, modifier = Modifier.fillMaxWidth())
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
        Box(modifier = Modifier.constrainAs(ref = indicatorRef) {
            width = Dimension.value(8.dp)
            height = Dimension.value(8.dp)
            start.linkTo(parent.start)
            top.linkTo(labelRef.top)
            centerVerticallyTo(labelRef)
        }) {

        }
        Text(text = item.label, modifier = Modifier.constrainAs(ref = labelRef) {
            width = Dimension.fillToConstraints
            start.linkTo(indicatorRef.start, 8.dp)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        })
        Text(text = item.description, modifier = Modifier.constrainAs(ref = descriptionRef) {
            width = Dimension.fillToConstraints
            start.linkTo(labelRef.start)
            end.linkTo(labelRef.end)
        })
    }
}