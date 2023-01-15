package com.amalcodes.wisescreen.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.ezrecyclerview.adapter.BaseAdapter
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.component.viewholder.*

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class MergeAdapter(
    initialData: List<ItemEntity> = listOf()
) : BaseAdapter<ItemEntity>(initialData.toMutableList()) {
    override fun onCreateBaseViewHolder(view: View, layoutRes: Int): RecyclerView.ViewHolder {
        return when (layoutRes) {
            R.layout.item_usage -> UsageItemViewHolder(view)
            R.layout.item_menu -> MenuItemViewHolder(view)
            R.layout.item_key_value_menu -> KeyValueMenuItemViewHolder(view)
            R.layout.item_day -> DayItemViewHolder(view)
            R.layout.item_app_limit -> AppLimitItemViewHolder(view)
            R.layout.item_divider -> DividerItemViewHolder(view)
            else -> throw IllegalStateException("unexpected View Holder for layoutRes: $layoutRes")
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ItemEntity>) {
        if (holder is ViewBindingViewHolder<*, *>) {
            holder.unbind()
        }
    }
}