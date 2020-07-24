package com.amalcodes.wisescreen.presentation.viewentity

import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 23/07/20
 */
 
 
data class DayItemViewEntity(
    val dayOfWeek: Int,
    var isSelected: Boolean = false
): ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_day
}