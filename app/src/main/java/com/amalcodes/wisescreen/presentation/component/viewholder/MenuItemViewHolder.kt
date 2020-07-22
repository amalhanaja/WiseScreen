package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemMenuBinding
import com.amalcodes.wisescreen.presentation.viewentity.MenuItemViewEntity

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class MenuItemViewHolder(
    view: View
) : ViewBindingViewHolder<MenuItemViewEntity, ItemMenuBinding>(view) {
    override fun onBind(entity: MenuItemViewEntity) = ItemMenuBinding.bind(itemView)
        .also { binding = it }
        .apply {
            tvTitle.text = entity.title
            tvDescription.text = entity.description
            ivIcon.setImageDrawable(entity.icon)
        }.run {

        }


    override fun onBindListener(entity: MenuItemViewEntity, listener: ViewHolderClickListener) {
        binding?.itemMenu?.setOnClickListener { listener.onClick(it, entity) }
    }
}