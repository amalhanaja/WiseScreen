package com.amalcodes.wisescreen.presentation

import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.presentation.viewentity.ItemUsageViewEntity
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


fun AppUsageEntity.toItemUsageViewEntity(): ItemUsageViewEntity = ItemUsageViewEntity(
    appName = appName,
    usageDuration = totalTimeInForeground,
    appIcon = appIcon
)

fun Throwable.toUIState(): UIState.UIFailure {
    Timber.e(this)
    return UIState.UIFailure.Unknown(this)
}