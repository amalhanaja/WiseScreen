package com.amalcodes.wisescreen.presentation.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartLegendItemViewEntity
import kotlin.properties.Delegates

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


class StackedBarChartLegend @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var data: List<StackedBarChartLegendItemViewEntity> by Delegates.observable(
        emptyList()
    ) { _, old, new ->
        setWillNotDraw(false)
        invalidate()
        requestLayout()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        orientation = HORIZONTAL
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        removeAllViews()
        weightSum = data.count().toFloat()
        data.forEachIndexed { index, item ->
            val viewItem = createItem(item)
            addView(viewItem, index)
        }
        setWillNotDraw(true)
    }

    private fun createItem(viewEntity: StackedBarChartLegendItemViewEntity): View {
        return StackedBarChartLegendItem(context).apply {
            this.viewEntity = viewEntity
            layoutParams = LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1f
            )
        }
    }
}