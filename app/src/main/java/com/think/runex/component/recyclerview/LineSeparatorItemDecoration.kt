package com.think.runex.component.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class LineSeparatorItemDecoration () : RecyclerView.ItemDecoration() {

    constructor(lineSeparator: Drawable?,
                addLineOnTopOrLeftOfFirstItem: Boolean = false,
                addLineOnBottomOrRightOfLastItem: Boolean = false,
                @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL) : this() {

        this.lineSeparator = lineSeparator
        this.addLineToTopOrLeftOfFirstItem = addLineOnTopOrLeftOfFirstItem
        this.addLineToBottomOrRightOfLastItem = addLineOnBottomOrRightOfLastItem
        this.orientation = orientation
    }

    @RecyclerView.Orientation
    var orientation: Int = RecyclerView.VERTICAL

    var lineSeparator: Drawable? = null

    var addLineToTopOrLeftOfFirstItem: Boolean = false
    var addLineToBottomOrRightOfLastItem: Boolean = false

    //var isIncludeParentPadding: Boolean = true

    @Dimension
    var marginLeftOrTop: Int = 0

    @Dimension
    var marginRightOrBottom: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val height: Int = lineSeparator?.intrinsicHeight ?: 0
        val width: Int = lineSeparator?.intrinsicWidth ?: 0

        when (parent.getChildLayoutPosition(view)) {
            //First item.
            0 -> when (orientation) {
                RecyclerView.VERTICAL -> {
                    if (addLineToTopOrLeftOfFirstItem) {
                        outRect.top = height
                    }
                    if (state.itemCount > 1 || addLineToBottomOrRightOfLastItem) {
                        outRect.bottom = height
                    }
                }
                RecyclerView.HORIZONTAL -> {
                    if (addLineToTopOrLeftOfFirstItem) {
                        outRect.left = width
                    }
                    if (addLineToBottomOrRightOfLastItem) {
                        outRect.right = width
                    }
                }
            }
            //Last item.
            (state.itemCount - 1) -> when (orientation) {
                RecyclerView.VERTICAL -> if (addLineToBottomOrRightOfLastItem) {
                    outRect.bottom = height
                }
                RecyclerView.HORIZONTAL -> if (addLineToBottomOrRightOfLastItem) {
                    outRect.right = width
                }
            }
            //Second item... N item.
            else -> when (orientation) {
                RecyclerView.VERTICAL -> outRect.bottom = height
                RecyclerView.HORIZONTAL -> outRect.right = width
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        if (lineSeparator == null || parent.childCount == 0) return

//        val left = parent.paddingLeft
//        val top = parent.paddingTop
//        val right = (parent.width - parent.paddingRight)
//        val bottom = (parent.height - parent.paddingBottom)
        val count: Int = parent.childCount

        for (i in 0 until count) {
            val child = parent.getChildAt(i)
            if (child != null) {
                val params = child.layoutParams as RecyclerView.LayoutParams

                val newLeft =  marginLeftOrTop

                val newTop = parent.top + marginLeftOrTop //top + if (isIncludeParentPadding) child.paddingTop else 0

                val newRight = parent.width - marginRightOrBottom

                val newBottom = parent.height - marginRightOrBottom//bottom - if (isIncludeParentPadding) child.paddingBottom else 0

                //Draw line separator to top or left of first position.
                if (i == 0 && addLineToTopOrLeftOfFirstItem) {
                    when (orientation) {
                        RecyclerView.VERTICAL -> {
                            val topOfLine = child.top - params.topMargin - lineSeparator!!.intrinsicHeight
                            val bottomOfLine = topOfLine + lineSeparator!!.intrinsicHeight
                            lineSeparator?.setBounds(newLeft, topOfLine, newRight, bottomOfLine)
                            lineSeparator?.draw(c)
                        }
                        RecyclerView.HORIZONTAL -> {
                            val leftOfLine = child.left - params.leftMargin - lineSeparator!!.intrinsicWidth
                            val rightOfLine = leftOfLine + lineSeparator!!.intrinsicWidth
                            lineSeparator?.setBounds(leftOfLine, newTop, rightOfLine, newBottom)
                            lineSeparator?.draw(c)
                        }
                    }
                }

                //Draw line separator at bottom or left of item 1..N item.
                if (i < (count - 1) || (i + 1) == count && addLineToBottomOrRightOfLastItem) {
                    when (orientation) {
                        RecyclerView.VERTICAL -> {
                            val topOfLine = child.bottom + params.bottomMargin
                            val bottomOfLine = topOfLine + lineSeparator!!.intrinsicHeight
                            lineSeparator?.setBounds(newLeft, topOfLine, newRight, bottomOfLine)
                            lineSeparator?.draw(c)
                        }
                        RecyclerView.HORIZONTAL -> {
                            val leftOfLine = child.right + params.rightMargin
                            val rightOfLine = leftOfLine + lineSeparator!!.intrinsicWidth
                            lineSeparator?.setBounds(leftOfLine, newTop, rightOfLine, newBottom)
                            lineSeparator?.draw(c)
                        }
                    }
                }
            }
        }
    }
}