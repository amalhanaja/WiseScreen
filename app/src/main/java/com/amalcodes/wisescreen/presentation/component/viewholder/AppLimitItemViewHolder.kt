package com.amalcodes.wisescreen.presentation.component.viewholder

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.getNullableApplicationIcon
import com.amalcodes.wisescreen.databinding.ItemAppLimitBinding
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


class AppLimitItemViewHolder(
    view: View
) : ViewBindingViewHolder<AppLimitViewEntity, ItemAppLimitBinding>(view) {

    override fun onBind(entity: AppLimitViewEntity) = ItemAppLimitBinding.bind(itemView)
        .also { binding = it }
        .run {
            tvLimitValue.text = when (entity.type) {
                AppLimitType.LIMIT_USE -> Util.formatTimeInMillis(
                    itemView.context,
                    entity.limitTimeInMillis.toLong()
                )
                AppLimitType.NEVER_ALLOW -> itemView.context.getString(R.string.text_Never_allowed)
                AppLimitType.ALWAYS_ALLOW -> itemView.context.getString(R.string.text_Always_allowed)
                AppLimitType.DEFAULT -> ""
            }
            tvName.text =
                itemView.context.packageManager.getApplicationName(packageName = entity.packageName)
            ivIcon.setImageDrawable(
                itemView.context.packageManager.getNullableApplicationIcon(
                    packageName = entity.packageName
                )
            )
        }

    override fun onBindListener(entity: AppLimitViewEntity, listener: ViewHolderClickListener) {
        binding?.itemAppLimit?.setOnClickListener { listener.onClick(it, entity) }
    }
}