package com.amalcodes.wisescreen.core

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApplicationInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getApplicationName(packageName: String): String {
        return context.packageManager.getApplicationName(packageName)
    }

    fun getApplicationIcon(packageName: String): Drawable? {
        return context.packageManager.getNullableApplicationIcon(packageName)
    }
}