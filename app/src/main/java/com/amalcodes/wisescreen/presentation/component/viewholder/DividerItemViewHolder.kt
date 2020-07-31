package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemDividerBinding
import com.amalcodes.wisescreen.presentation.viewentity.DividerItemViewEntity

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


class DividerItemViewHolder(
    view: View
) : ViewBindingViewHolder<DividerItemViewEntity, ItemDividerBinding>(view) {

    override fun onBind(entity: DividerItemViewEntity) = ItemDividerBinding.bind(itemView)
        .also { binding = it }
        .run { divider.setBackgroundColor(entity.backgroundColor) }

    override fun onBindListener(entity: DividerItemViewEntity, listener: ViewHolderClickListener) {
    }
}