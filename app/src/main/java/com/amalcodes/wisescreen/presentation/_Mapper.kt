package com.amalcodes.wisescreen.presentation

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.error.AppBlockedError
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity
import com.amalcodes.wisescreen.presentation.viewentity.UsageItemViewEntity

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


fun AppUsageEntity.toItemUsageViewEntity(totalUsage: Int): UsageItemViewEntity =
    UsageItemViewEntity(
        appName = appName,
        usageDuration = totalTimeInForeground.toInt(),
        appIcon = null,
        totalUsage = totalUsage
    )

fun AppLimitViewEntity.toAppLimitEntity(): AppLimitEntity = AppLimitEntity(
    id = id,
    packageName = packageName,
    type = type,
    limitTimeInMillis = limitTimeInMillis
)

fun Throwable.toUIState(): UIState.UIFailure {
    return when (this) {
        is AppBlockedError -> UIState.UIFailure.RequiredPin
        else -> UIState.UIFailure.Unknown(this)
    }
}