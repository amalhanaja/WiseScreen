package com.amalcodes.wisescreen.presentation.component

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.databinding.ComponentItemStackedBarChartLegendBinding
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartLegendItemViewEntity
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


class StackedBarChartLegendItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ComponentItemStackedBarChartLegendBinding? = null

    var viewEntity: StackedBarChartLegendItemViewEntity? = null
        set(value) {
            field = value
            bindToView(value)
            invalidate()
            requestLayout()
        }

    private fun bindToView(value: StackedBarChartLegendItemViewEntity?) {
        Timber.d("Bind To View ${binding == null}")
        value?.let {
            binding?.ivIndicator?.imageTintList = ColorStateList.valueOf(it.color)
            binding?.tvName?.text = it.name
            binding?.tvDuration?.text = Util.formatTimeInMillis(context, it.duration.toLong())
        }
    }

    init {
        binding = ComponentItemStackedBarChartLegendBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }
}