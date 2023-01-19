package com.amalcodes.wisescreen.features.screentime.applimit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.getNullableApplicationIcon
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@ExperimentalMaterial3Api
@Composable
fun AppLimitPage(
    appLimitUiState: AppLimitUiState,
    query: String,
    setQuery: (String) -> Unit,
    goToAppLimitSelection: (AppLimitEntity) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Space16),
            value = query,
            onValueChange = setQuery,
            leadingIcon = {
                Image(painter = painterResource(id = R.drawable.ic_search), contentDescription = "Search")
            },
            trailingIcon = trailingIcon@{
                if (query.isBlank()) return@trailingIcon
                IconButton(onClick = { setQuery("") }) {
                    Image(painter = painterResource(id = R.drawable.ic_close), contentDescription = "Clear")
                }
            }
        )
        when (appLimitUiState) {
            is AppLimitUiState.Empty -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            is AppLimitUiState.WithData -> LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(items = appLimitUiState.data) { index, item ->
                    AppLimitItem(
                        appLimit = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { goToAppLimitSelection(item) }
                            .padding(vertical = SpacingTokens.Space12),
                    )
                    if (index != appLimitUiState.data.count() - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 74.dp)
                        ) {
                            Divider()
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun AppLimitItem(
    modifier: Modifier = Modifier,
    appLimit: AppLimitEntity,
) {
    val context = LocalContext.current
    val appName = context.packageManager.getApplicationName(appLimit.packageName)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(SpacingTokens.Space16))
        Image(
            modifier = Modifier.size(42.dp),
            painter = rememberDrawablePainter(drawable = context.packageManager.getNullableApplicationIcon(appLimit.packageName)),
            contentDescription = appName
        )
        Spacer(modifier = Modifier.width(SpacingTokens.Space16))
        Text(
            text = appName,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(SpacingTokens.Space4))
        Text(
            text = when (appLimit.type) {
                AppLimitType.LIMIT_USE -> Util.formatTimeInMillis(context, appLimit.limitTimeInMillis.toLong())
                AppLimitType.NEVER_ALLOW -> stringResource(id = R.string.text_Never_allowed)
                AppLimitType.ALWAYS_ALLOW -> stringResource(id = R.string.text_Always_allowed)
                AppLimitType.DEFAULT -> ""
            }
        )
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = appName,
        )
    }
}