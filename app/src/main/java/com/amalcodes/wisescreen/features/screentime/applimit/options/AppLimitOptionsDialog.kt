package com.amalcodes.wisescreen.features.screentime.applimit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.presentation.components.TimePickerComponent
import com.amalcodes.wisescreen.presentation.foundation.SpacingTokens

@ExperimentalMaterial3Api
@Composable
fun AppLimitOptionsDialog(
    appLimitEntity: AppLimitEntity,
    onSubmit: (AppLimitEntity) -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val (mutableAppLimitEntity, setMutableAppLimitEntity) = remember { mutableStateOf(appLimitEntity) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(SpacingTokens.Space16))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.Space16),
            text = context.packageManager.getApplicationName(mutableAppLimitEntity.packageName),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(SpacingTokens.Space8))
        AppLimitSelectionItem(
            text = stringResource(id = R.string.text_Limit_use),
            selected = mutableAppLimitEntity.type == AppLimitType.LIMIT_USE,
            onClick = { setMutableAppLimitEntity(mutableAppLimitEntity.copy(type = AppLimitType.LIMIT_USE)) }
        )
        AnimatedVisibility(visible = mutableAppLimitEntity.type == AppLimitType.LIMIT_USE) {
            TimePickerComponent(
                modifier = Modifier.fillMaxWidth(),
                millis = mutableAppLimitEntity.limitTimeInMillis,
                onChange = { setMutableAppLimitEntity(mutableAppLimitEntity.copy(limitTimeInMillis = it)) }
            )
        }
        Divider()
        AppLimitSelectionItem(
            text = stringResource(id = R.string.text_Always_allowed),
            selected = mutableAppLimitEntity.type == AppLimitType.ALWAYS_ALLOW,
            onClick = { setMutableAppLimitEntity(mutableAppLimitEntity.copy(type = AppLimitType.ALWAYS_ALLOW)) }
        )
        Divider()
        AppLimitSelectionItem(
            text = stringResource(id = R.string.text_Never_allowed),
            selected = mutableAppLimitEntity.type == AppLimitType.NEVER_ALLOW,
            onClick = { setMutableAppLimitEntity(mutableAppLimitEntity.copy(type = AppLimitType.NEVER_ALLOW)) }
        )
        Divider()
        AppLimitSelectionItem(
            text = stringResource(id = R.string.text_Default),
            selected = mutableAppLimitEntity.type == AppLimitType.DEFAULT,
            onClick = { setMutableAppLimitEntity(mutableAppLimitEntity.copy(type = AppLimitType.DEFAULT)) }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Space16)
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(id = R.string.text_Cancel))
            }
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { onSubmit(mutableAppLimitEntity) }
            ) {
                Text(text = stringResource(id = R.string.text_OK))
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AppLimitSelectionItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        headlineText = { Text(text = text) },
        trailingContent = {
            RadioButton(
                interactionSource = interactionSource,
                selected = selected,
                onClick = onClick,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}