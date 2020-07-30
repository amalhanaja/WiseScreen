package com.amalcodes.wisescreen.presentation.component


import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.wisescreen.R
import kotlin.math.roundToInt


/**
 * @author Aldyaz Nugroho on 16/03/20.
 * https://github.com/aldyaz
 */

class DividerItemDecoration(
    context: Context,
    private val orientation: Int = LinearLayoutManager.VERTICAL,
    drawableDivider: Int = R.drawable.default_list_divider,
    private val dividerMarginLeft: Float? = null
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = LinearLayoutManager.HORIZONTAL
        const val VERTICAL = LinearLayoutManager.VERTICAL
    }

    private val divider: Drawable by lazy {
        requireNotNull(ContextCompat.getDrawable(context, drawableDivider))
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            VERTICAL -> drawVertical(c, parent)
            HORIZONTAL -> drawHorizontal(c, parent)
            else -> throw IllegalStateException("Unsupported")
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = dividerMarginLeft?.roundToInt() ?: parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0..childCount - 2) {
            val child = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams =
                child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams =
                child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }

    }

}