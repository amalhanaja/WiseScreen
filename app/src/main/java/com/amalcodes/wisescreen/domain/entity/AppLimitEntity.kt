package com.amalcodes.wisescreen.domain.entity

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


data class AppLimitEntity(
    val id: Long = 0,
    val packageName: String,
    val limitTimeInMillis: Int,
    val type: AppLimitType
)