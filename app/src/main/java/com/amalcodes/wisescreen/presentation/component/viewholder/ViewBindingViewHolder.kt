package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import androidx.viewbinding.ViewBinding
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder

/**
 * @author: AMAL
 * Created On : 18/07/20
 */
 
 
abstract class ViewBindingViewHolder<Entity: ItemEntity, VB: ViewBinding>(
    view: View
) : BaseViewHolder<Entity>(view) {

    var binding: VB? = null

    fun unbind() {
        binding = null
    }

}