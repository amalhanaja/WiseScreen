package com.amalcodes.wisescreen.presentation.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartItemViewEntity
import kotlin.properties.Delegates

/**
 * @author: AMAL
 * Created On : 22/07/20
 */


class StackedBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var data: List<StackedBarChartItemViewEntity> by Delegates.observable(
        listOf()
    ) { _, _, _ ->
        setWillNotDraw(false)
        invalidate()
        requestLayout()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        orientation = HORIZONTAL
        weightSum = 100f
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        removeAllViews()
        data.forEachIndexed { index, item ->
            val viewItem = createItem(index, item)
            addView(viewItem, index)
        }
        setWillNotDraw(true)
    }

    private fun createItem(index: Int, viewEntity: StackedBarChartItemViewEntity): View {
        val (percentage, color) = viewEntity
        return View(context).apply {
            layoutParams = LayoutParams(
                0,
                this@StackedBarChart.layoutParams.height,
                percentage
            ).apply {
                marginStart = if (index == 0) 0 else resources.getDimensionPixelSize(R.dimen.space__0_125x)
                marginEnd = if (index == data.lastIndex) 0 else resources.getDimensionPixelSize(R.dimen.space__0_125x)
            }
            setBackgroundResource(R.drawable.bg_round_1x)
            backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}