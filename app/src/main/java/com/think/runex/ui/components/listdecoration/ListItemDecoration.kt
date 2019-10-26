package com.think.runex.ui.components.listdecoration

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

class ListItemDecoration(
        @Dimension private var marginList: Int = 0,
        @SuppressLint("SupportAnnotationUsage")
        @DrawableRes private var lineDivider: Drawable? = null,
        private var withTopOfFirstItem: Boolean = true,
        private var withBottomOfLastItem: Boolean = true,
        private var withStartAndEnd: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        //Start and End Offsets
        if (withStartAndEnd && marginList > 0) {
            outRect.left = marginList
            outRect.right = marginList
        }

        //Update margin top on first item and margin bottom on last item.
        //By default will be set margin or drawable between each a items.
        when (parent.getChildLayoutPosition(view)) {
            0 -> {
                //First item.
                var outRectTop = 0
                if (withTopOfFirstItem) {
                    outRectTop = (outRectTop + marginList)
                }
                if (lineDivider != null && withTopOfFirstItem) {
                    outRectTop = (outRectTop + lineDivider!!.intrinsicHeight)
                }
                if (outRectTop > 0) {
                    outRect.top = outRectTop
                }

                val outRectBottom = getOutRectBottom()
                if (outRectBottom > 0) {
                    outRect.bottom = outRectBottom
                }
            }
            state.itemCount.minus(1) -> {
                //Last item.
                var outRectBottom = 0
                if (withBottomOfLastItem) {
                    outRectBottom = (outRectBottom + marginList)
                }
                if (lineDivider != null && withBottomOfLastItem) {
                    outRectBottom = (outRectBottom + lineDivider!!.intrinsicHeight)
                }
                if (outRectBottom > 0) {
                    outRect.bottom = outRectBottom
                }
            }
            else -> {
                //Second item... N item.
                val outRectBottom = getOutRectBottom()
                if (outRectBottom > 0) {
                    outRect.bottom = outRectBottom
                }
            }
        }
    }

    private fun getOutRectBottom(): Int {
        var outRectBottom = marginList
        if (lineDivider != null) {
            outRectBottom = (outRectBottom + lineDivider!!.intrinsicHeight)
        }
        return outRectBottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        if (lineDivider != null && parent.childCount > 0) {
            val left = parent.paddingLeft
            val right = (parent.width - parent.paddingRight)
            val childCount: Int = parent.childCount

            for (i in 0 until childCount - 1) {
                parent.getChildAt(i)?.let { child ->
                    val params = child.layoutParams as RecyclerView.LayoutParams

                    if (i == 0 && withTopOfFirstItem && childCount > 1) {
                        val topFirst = child.top - lineDivider!!.intrinsicHeight
                        val bottomFirst = topFirst + lineDivider!!.intrinsicHeight
                        lineDivider?.setBounds(left, topFirst, right, bottomFirst)
                        lineDivider?.draw(c)
                    }

                    if (i < (childCount - 1)) {
                        val top = child.bottom + params.bottomMargin
                        val bottom = top + lineDivider!!.intrinsicHeight
                        lineDivider?.setBounds(left, top, right, bottom)
                        lineDivider?.draw(c)
                    }
                }
            }
        }
    }
}