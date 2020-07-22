package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemKeyValueMenuBinding
import com.amalcodes.wisescreen.presentation.viewentity.KeyValueMenuItemViewEntity

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class KeyValueMenuItemViewHolder(
    view: View
) : ViewBindingViewHolder<KeyValueMenuItemViewEntity, ItemKeyValueMenuBinding>(view) {
    override fun onBind(entity: KeyValueMenuItemViewEntity) = ItemKeyValueMenuBinding.bind(itemView)
        .also { binding = it }
        .run {
            val (key, value) = entity
            itemKeyValueMenu.tag = adapterPosition
            tvKey.text = key
            tvValue.text = value
        }

    override fun onBindListener(
        entity: KeyValueMenuItemViewEntity,
        listener: ViewHolderClickListener
    ) {
        binding?.itemKeyValueMenu?.setOnClickListener { listener.onClick(it, entity) }
    }
}