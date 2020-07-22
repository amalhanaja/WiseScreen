package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.databinding.ItemUsageBinding
import com.amalcodes.wisescreen.presentation.viewentity.ItemUsageViewEntity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class ItemUsageViewHolder(
    view: View
) : ViewBindingViewHolder<ItemUsageViewEntity, ItemUsageBinding>(view) {

    override fun onBind(entity: ItemUsageViewEntity) = ItemUsageBinding.bind(itemView)
        .also(this::setBinding)
        .let { binding ->
            binding.ivIcon.setImageDrawable(entity.appIcon)
            binding.tvName.text = entity.appName
            val minute = TimeUnit.MILLISECONDS.toMinutes(entity.usageDuration)
            binding.tvUsageTime.text = if (minute == 0L ) "< 0 m" else "$minute m"
        }

    override fun onBindListener(entity: ItemUsageViewEntity, listener: ViewHolderClickListener) {

    }


}