package com.amalcodes.wisescreen.presentation.viewentity

import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


data class KeyValueMenuItemViewEntity(
    val key: String,
    val value: String
): ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_key_value_menu
}