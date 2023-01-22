package com.amalcodes.wisescreen.features.appusage

import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun AppUsagePage(
    appUsageUiState: AppUsageUiState,
    pagerState: PagerState = rememberPagerState(),
) {
    val tabs = remember { TimeRangeEnum.values() }
    val coroutineScope = rememberCoroutineScope()
    Column() {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            divider = { }
        ) {
            tabs.forEach {
                Tab(
                    selected = it.ordinal == pagerState.currentPage,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(it.ordinal) }
                    },
                    text = {
                        Text(
                            text = when (it) {
                                TimeRangeEnum.TODAY -> stringResource(id = R.string.text_Today)
                                TimeRangeEnum.THIS_WEEK -> stringResource(id = R.string.text_Last_7_days)
                            }
                        )
                    },
                    unselectedContentColor = LocalContentColor.current.copy(alpha = 0.48f)
                )
            }
        }
        HorizontalPager(
            pageCount = tabs.count(),
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            when (appUsageUiState) {
                is AppUsageUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
                is AppUsageUiState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val pageData = appUsageUiState.data[page]
                    item {
                        AppUsageSummary(
                            modifier = Modifier.padding(SpacingTokens.Space16),
                            totalUsage = pageData.totalUsage,
                            percentage = pageData.percentage,
                            start = pageData.start,
                            end = pageData.end,
                        )
                    }
                    item { Divider(thickness = SpacingTokens.Space8) }
                    itemsIndexed(pageData.usages) { _, item ->
                        ListItem(
                            modifier = Modifier,
                            leadingContent = {
                                Image(
                                    painter = rememberDrawablePainter(drawable = item.icon),
                                    contentDescription = item.appName,
                                    modifier = Modifier.size(32.dp),
                                )
                            },
                            headlineText = {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = item.appName,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                    )
                                    Text(
                                        text = item.totalTimeUsage,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            },
                            supportingText = {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = SpacingTokens.Space8),
                                    progress = item.progress,
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppUsageSummary(
    modifier: Modifier = Modifier,
    totalUsage: String,
    percentage: Float,
    start: String,
    end: String,
) {
    Column(modifier = modifier) {
        Text(text = stringResource(id = R.string.text_Screen_Time), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(SpacingTokens.Space4))
        Text(text = totalUsage, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(SpacingTokens.Space8))
        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space4))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = start, style = MaterialTheme.typography.bodySmall)
            Text(text = end, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Immutable
data class AppUsageListItem(
    val appName: String,
    val icon: Drawable?,
    val progress: Float,
    val totalTimeUsage: String,
)
