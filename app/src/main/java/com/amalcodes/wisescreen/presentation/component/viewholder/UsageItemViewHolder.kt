package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.databinding.ItemUsageBinding
import com.amalcodes.wisescreen.presentation.viewentity.UsageItemViewEntity

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class UsageItemViewHolder(
    view: View
) : ViewBindingViewHolder<UsageItemViewEntity, ItemUsageBinding>(view) {

    override fun onBind(entity: UsageItemViewEntity) = ItemUsageBinding.bind(itemView)
        .also { binding = it }
        .let { binding ->
            binding.ivIcon.setImageDrawable(entity.appIcon)
            binding.tvName.text = entity.appName
            binding.tvUsageTime.text = Util.formatTimeInMillis(
                itemView.context,
                entity.usageDuration.toLong()
            )
            binding.pbScreenTime.max = entity.totalUsage
            binding.pbScreenTime.progress = entity.usageDuration
        }

    override fun onBindListener(entity: UsageItemViewEntity, listener: ViewHolderClickListener) {

    }


}