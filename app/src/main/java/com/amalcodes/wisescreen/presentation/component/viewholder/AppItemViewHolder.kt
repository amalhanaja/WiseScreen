package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemAppBinding
import com.amalcodes.wisescreen.presentation.viewentity.AppItemViewEntity

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


class AppItemViewHolder(
    view: View
) : ViewBindingViewHolder<AppItemViewEntity, ItemAppBinding>(view) {

    override fun onBind(entity: AppItemViewEntity) = ItemAppBinding.bind(itemView)
        .also { binding = it }
        .run {
            tvName.text = entity.appName
            ivIcon.setImageDrawable(entity.appIcon)
        }

    override fun onBindListener(entity: AppItemViewEntity, listener: ViewHolderClickListener) {
        binding?.itemApp?.setOnClickListener { listener.onClick(it, entity) }
    }
}