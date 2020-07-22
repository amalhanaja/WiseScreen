package com.amalcodes.wisescreen.presentation.viewentity

import android.graphics.drawable.Drawable
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


data class MenuItemViewEntity(
    val title: String,
    val description: String,
    val icon: Drawable
): ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_menu
}