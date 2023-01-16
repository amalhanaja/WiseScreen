package com.amalcodes.wisescreen.core

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceGetter @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getString(@StringRes resId: Int, vararg formatArgs: Any = emptyArray()): String {
        return context.getString(resId, *formatArgs)
    }
}