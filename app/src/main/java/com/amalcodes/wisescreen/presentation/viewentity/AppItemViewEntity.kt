package com.amalcodes.wisescreen.presentation.viewentity

import android.graphics.drawable.Drawable
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


data class AppItemViewEntity(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable?
): ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_app
}