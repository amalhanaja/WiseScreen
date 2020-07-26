package com.amalcodes.wisescreen.data

import com.amalcodes.wisescreen.data.entity.AppLimitDataEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType

/**
 * @author: AMAL
 * Created On : 25/07/20
 */
 
 
fun AppLimitDataEntity.toAppLimitEntity(): AppLimitEntity = AppLimitEntity(
    id = id,
    packageName = packageName,
    limitTimeInMillis = limitTimeInMillis,
    type = AppLimitType.valueOf(type)
)

fun AppLimitEntity.toAppLimitDataEntity(): AppLimitDataEntity = AppLimitDataEntity(
    id = id,
    packageName = packageName,
    limitTimeInMillis = limitTimeInMillis,
    type = type.name
)