package com.amalcodes.wisescreen.presentation.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmapOrNull
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.getNullableApplicationIcon
import com.amalcodes.wisescreen.presentation.tokens.SpacingTokens

@Immutable
data class AppUsageComponentState(
    val packageName: String,
    val appName: String,
    val image: Drawable,
    val usageDuration: Long,
    val totalUsage: Long,
)

@Composable
fun AppUsageComponent(
    appUsageComponentState: AppUsageComponentState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val formattedUsageDuration = remember { Util.formatTimeInMillis(context, appUsageComponentState.usageDuration) }
    val usageProgress: Float = remember { appUsageComponentState.usageDuration.toFloat() / appUsageComponentState.totalUsage.toFloat() }
    val appIcon = remember {
        context.packageManager.getNullableApplicationIcon(appUsageComponentState.packageName)
            ?.toBitmapOrNull()?.asImageBitmap()?.let { BitmapPainter(it) }
    }
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (iconRef, nameRef, usageRef, barRef) = createRefs()
        Image(
            modifier = Modifier.constrainAs(ref = iconRef) {
                linkTo(top = parent.top, bottom = parent.bottom, bottomMargin = SpacingTokens.Space12, topMargin = SpacingTokens.Space12)
                width = Dimension.value(32.dp)
                height = Dimension.value(32.dp)
            },
            painter = appIcon ?: painterResource(id = R.drawable.il_img_placeholder),
            contentDescription = appUsageComponentState.appName,
        )
        Text(
            modifier = Modifier.constrainAs(ref = nameRef) {
                start.linkTo(iconRef.end, SpacingTokens.Space16)
                top.linkTo(parent.top)
                end.linkTo(usageRef.start, SpacingTokens.Space8)
                bottom.linkTo(barRef.top)
                width = Dimension.fillToConstraints
            },
            text = appUsageComponentState.appName,
            style = MaterialTheme.typography.titleMedium
        )
        LinearProgressIndicator(
            modifier = Modifier.constrainAs(ref = barRef) {
                start.linkTo(nameRef.start)
                top.linkTo(nameRef.bottom, SpacingTokens.Space4)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            progress = usageProgress
        )
        Text(
            modifier = Modifier.constrainAs(ref = usageRef) {
                centerVerticallyTo(nameRef)
                end.linkTo(parent.end)
            },
            text = formattedUsageDuration,
            style = MaterialTheme.typography.bodyMedium
        )
        createVerticalChain(
            elements = arrayOf(nameRef, barRef),
            chainStyle = ChainStyle.Packed
        )
    }
}