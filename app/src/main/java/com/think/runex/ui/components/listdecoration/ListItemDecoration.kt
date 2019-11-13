package com.think.runex.ui.components.listdecoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class ListItemDecoration() : RecyclerView.ItemDecoration() {

    constructor(@Dimension marginSpace: Int,
                marginTopOfFirstItem: Boolean = false,
                marginBottomOfLastItem: Boolean = false,
                marginStartAndEnd: Boolean = false) : this() {

        this.marginSpace = marginSpace
        this.withTopOfFirstItem = marginTopOfFirstItem
        this.withBottomOfLastItem = marginBottomOfLastItem
        this.withStartAndEnd = marginStartAndEnd
    }

    constructor(marginDrawable: Drawable,
                marginTopOfFirstItem: Boolean = false,
                marginBottomOfLastItem: Boolean = false,
                marginStartAndEnd: Boolean = false) : this() {

        this.marginDrawable = marginDrawable
        this.withTopOfFirstItem = marginTopOfFirstItem
        this.withBottomOfLastItem = marginBottomOfLastItem
        this.withStartAndEnd = marginStartAndEnd
    }

    @Dimension
    private var marginSpace: Int = 0
    private var marginDrawable: Drawable? = null
    private var withTopOfFirstItem: Boolean = true
    private var withBottomOfLastItem: Boolean = true
    private var withStartAndEnd: Boolean = false


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        //Start and End Offsets
        if (withStartAndEnd && marginSpace > 0) {
            outRect.left = marginSpace
            outRect.right = marginSpace
        }

        //Update margin top on first item and margin bottom on last item.
        //By default will be set margin or drawable between each a items.
        when (parent.getChildLayoutPosition(view)) {
            0 -> {
                //First item.
                var outRectTop = 0
                if (withTopOfFirstItem) {
                    outRectTop = (outRectTop + marginSpace)
                }
                if (marginDrawable != null && withTopOfFirstItem) {
                    outRectTop = (outRectTop + marginDrawable!!.intrinsicHeight)
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
                    outRectBottom = (outRectBottom + marginSpace)
                }
                if (marginDrawable != null && withBottomOfLastItem) {
                    outRectBottom = (outRectBottom + marginDrawable!!.intrinsicHeight)
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
        var outRectBottom = marginSpace
        if (marginDrawable != null) {
            outRectBottom = (outRectBottom + marginDrawable!!.intrinsicHeight)
        }
        return outRectBottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        if (marginDrawable != null && parent.childCount > 0) {
            val left = parent.paddingLeft
            val right = (parent.width - parent.paddingRight)
            val childCount: Int = parent.childCount

            for (i in 0 until childCount - 1) {
                parent.getChildAt(i)?.let { child ->
                    val params = child.layoutParams as RecyclerView.LayoutParams

                    if (i == 0 && withTopOfFirstItem && childCount > 1) {
                        val topFirst = child.top - marginDrawable!!.intrinsicHeight
                        val bottomFirst = topFirst + marginDrawable!!.intrinsicHeight
                        marginDrawable?.setBounds(left, topFirst, right, bottomFirst)
                        marginDrawable?.draw(c)
                    }

                    if (i < (childCount - 1)) {
                        val top = child.bottom + params.bottomMargin
                        val bottom = top + marginDrawable!!.intrinsicHeight
                        marginDrawable?.setBounds(left, top, right, bottom)
                        marginDrawable?.draw(c)
                    }
                }
            }
        }
    }
}