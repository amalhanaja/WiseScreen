package com.amalcodes.wisescreen.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApplicationInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getApplicationName(packageName: String): String {
        return context.packageManager.getApplicationName(packageName)
    }
}