package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemUsageBinding
import com.amalcodes.wisescreen.presentation.viewentity.UsageItemViewEntity
import java.util.concurrent.TimeUnit

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
            val minute = TimeUnit.MILLISECONDS.toMinutes(entity.usageDuration)
            binding.tvUsageTime.text = if (minute == 0L ) "< 0 m" else "$minute m"
        }

    override fun onBindListener(entity: UsageItemViewEntity, listener: ViewHolderClickListener) {

    }


}