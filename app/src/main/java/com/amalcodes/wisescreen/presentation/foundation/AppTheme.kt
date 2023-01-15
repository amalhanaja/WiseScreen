package com.amalcodes.wisescreen.presentation.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.color.DynamicColors

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val isDynamicColorAvailable = DynamicColors.isDynamicColorAvailable()
    val context = LocalContext.current
    MaterialTheme(
        colorScheme = when {
            isDynamicColorAvailable && isDark -> dynamicDarkColorScheme(context)
            isDynamicColorAvailable -> dynamicLightColorScheme(context)
            else -> lightColorScheme(
                primary = ColorPalettes.PersianGreen,
                primaryContainer = ColorPalettes.PersianGreen
            )
        },
        content = content,
        typography = MaterialTheme.typography.copy()
    )
}