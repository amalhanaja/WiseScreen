package com.amalcodes.wisescreen.features.blocked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.domain.entity.AppBlockedType
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Immutable
data class AppBlockedPageState(
    val appBlockedType: AppBlockedType,
    val appName: String,
)

@Composable
fun AppBlockedPage(
    appBlockedPageState: AppBlockedPageState,
    goToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val message: String = when (appBlockedPageState.appBlockedType) {
        AppBlockedType.NEVER_ALLOWED -> stringResource(R.string.text_restricted_app, appBlockedPageState.appName)
        AppBlockedType.DAILY_TIME_LIMIT, AppBlockedType.APP_LIMIT -> stringResource(R.string.text_app_limited, appBlockedPageState.appName)
    }
    val context = LocalContext.current
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (imageRef, titleRef, descriptionRef, btnRef) = createRefs()
        Image(
            painter = rememberDrawablePainter(drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)),
            contentDescription = message,
            modifier = Modifier.constrainAs(ref = imageRef) {
                width = Dimension.wrapContent
                top.linkTo(parent.top, SpacingTokens.Space24)
                centerHorizontallyTo(parent)
            }
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.constrainAs(ref = titleRef) {
                centerHorizontallyTo(parent)
                top.linkTo(imageRef.bottom, SpacingTokens.Space64)
            }
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(descriptionRef) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = SpacingTokens.Space24,
                    endMargin = SpacingTokens.Space24
                )
                top.linkTo(titleRef.bottom, SpacingTokens.Space16)
            }
        )
        /* btnOk:
         if never allowed -> Ok
         if app limit or daily limit -> get more time -> then open dialog 15 more minute
        * */
        TextButton(
            modifier = Modifier.constrainAs(ref = btnRef) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom, SpacingTokens.Space32)
            },
            onClick = goToMain,
        ) {
            Text(text = stringResource(id = R.string.text_OK))
        }
    }
}