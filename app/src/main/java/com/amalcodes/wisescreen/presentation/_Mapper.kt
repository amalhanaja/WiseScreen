package com.amalcodes.wisescreen.presentation

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity
import com.amalcodes.wisescreen.presentation.viewentity.UsageItemViewEntity
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


fun AppUsageEntity.toItemUsageViewEntity(totalUsage: Int): UsageItemViewEntity = UsageItemViewEntity(
    appName = appName,
    usageDuration = totalTimeInForeground.toInt(),
    appIcon = appIcon,
    totalUsage = totalUsage
)

fun AppLimitEntity.toAppLimitViewEntity(): AppLimitViewEntity = AppLimitViewEntity(
    id = id,
    packageName = packageName,
    type = type,
    limitTimeInMillis = limitTimeInMillis
)

fun AppLimitViewEntity.toAppLimitEntity(): AppLimitEntity = AppLimitEntity(
    id = id,
    packageName = packageName,
    type = type,
    limitTimeInMillis = limitTimeInMillis
)

fun Throwable.toUIState(): UIState.UIFailure {
    Timber.e(this)
    return UIState.UIFailure.Unknown(this)
}