package com.amalcodes.wisescreen.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.ezrecyclerview.adapter.BaseAdapter
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.component.viewholder.ItemUsageViewHolder
import com.amalcodes.wisescreen.presentation.component.viewholder.ViewBindingViewHolder

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class MergeAdapter(
    initialData: List<ItemEntity> = listOf()
): BaseAdapter<ItemEntity>(initialData.toMutableList()) {
    override fun onCreateBaseViewHolder(view: View, layoutRes: Int): RecyclerView.ViewHolder {
        return when (layoutRes) {
            R.layout.item_usage -> ItemUsageViewHolder(view)
            else -> throw IllegalStateException("unexpected View Holder for layoutRes: $layoutRes")
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ItemEntity>) {
        if (holder is ViewBindingViewHolder<*, *>) {
            holder.unbind()
        }
    }
}