package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.core.clearTime
import com.amalcodes.wisescreen.databinding.ItemDayBinding
import com.amalcodes.wisescreen.presentation.viewentity.DayItemViewEntity
import java.util.*

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class DayItemViewHolder(
    view: View
) : ViewBindingViewHolder<DayItemViewEntity, ItemDayBinding>(view) {
    override fun onBind(entity: DayItemViewEntity) = ItemDayBinding.bind(itemView)
        .also { binding = it }
        .run {
            val cal = Calendar.getInstance().apply {
                clearTime()
                this[Calendar.DAY_OF_WEEK] = entity.dayOfWeek
            }
            checkBox.isChecked = entity.isSelected
            tvDayName.text = cal.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                Locale.getDefault()
            )
        }

    override fun onBindListener(entity: DayItemViewEntity, listener: ViewHolderClickListener) {
        binding?.checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            listener.onClick(buttonView, entity.copy(isSelected = isChecked))
        }
        binding?.itemDay?.setOnClickListener {
            binding?.checkBox?.isChecked = !(binding?.checkBox?.isChecked ?: false)
        }
    }
}