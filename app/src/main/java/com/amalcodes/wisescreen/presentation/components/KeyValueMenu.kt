package com.amalcodes.wisescreen.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

@Composable
fun KeyValueMenu(
    keyValue: Pair<String, String>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = keyValue.first,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.width(SpacingTokens.Space16))
        Text(
            modifier = Modifier.weight(1f),
            text = keyValue.second,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
        )
        Image(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = keyValue.first,
        )
    }
}