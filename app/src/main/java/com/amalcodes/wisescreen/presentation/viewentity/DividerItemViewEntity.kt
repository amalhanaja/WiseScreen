package com.amalcodes.wisescreen.presentation.viewentity

import androidx.annotation.ColorInt
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.wisescreen.R

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


data class DividerItemViewEntity(
    @ColorInt
    val backgroundColor: Int
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_divider


}