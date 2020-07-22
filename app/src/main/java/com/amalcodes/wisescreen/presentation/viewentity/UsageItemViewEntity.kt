package com.amalcodes.wisescreen.presentation.viewentity

import android.graphics.drawable.Drawable
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 18/07/20
 */
 
 
data class UsageItemViewEntity(
    val appName: String,
    val usageDuration: Long,
    val appIcon: Drawable?
): ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_usage
}