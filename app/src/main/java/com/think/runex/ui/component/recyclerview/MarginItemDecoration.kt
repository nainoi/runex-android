package com.think.runex.ui.component.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration() : RecyclerView.ItemDecoration() {

    constructor(@Dimension marginBetweenItems: Int,
                @Dimension marginLeftOrTop: Int = 0,
                @Dimension marginRightOrBottom: Int = 0,
                @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL) : this() {

        this.marginBetweenItems = marginBetweenItems
        this.marginLeftOrTop = marginLeftOrTop
        this.marginRightOrBottom = marginRightOrBottom
        this.orientation = orientation
    }

    constructor(@Dimension margin: Int,
                @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL) : this() {
        this.marginBetweenItems = margin
        this.marginLeftOrTop = margin
        this.marginRightOrBottom = margin
        this.orientation = orientation
    }


    @RecyclerView.Orientation
    var orientation: Int = RecyclerView.VERTICAL

    @Dimension
    var marginBetweenItems: Int = 0

    @Dimension
    var marginLeftOrTop: Int = 0

    @Dimension
    var marginRightOrBottom: Int = 0

    var marginTopOrLeftOfFirstItem: Boolean = true
    var marginBottomOrRightOfLastItem: Boolean = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        when (orientation) {
            RecyclerView.VERTICAL -> {
                //Update start offset
                if (marginLeftOrTop > 0) {
                    outRect.left = marginLeftOrTop
                }
                //Update end offset
                if (marginRightOrBottom > 0) {
                    outRect.right = marginRightOrBottom
                }
            }
            RecyclerView.HORIZONTAL -> {
                //Update top offset
                if (marginLeftOrTop > 0) {
                    outRect.top = marginLeftOrTop
                }
                //Update bottom offset
                if (marginRightOrBottom > 0) {
                    outRect.bottom = marginRightOrBottom
                }
            }
        }

        when (parent.getChildLayoutPosition(view)) {
            //First item.
            0 -> when (orientation) {
                RecyclerView.VERTICAL -> {
                    //Update top of first item
                    val newTop = outRect.top + (if (marginTopOrLeftOfFirstItem) marginBetweenItems else 0)
                    if (newTop > 0) {
                        outRect.top = newTop
                    }
                    //Update bottom of first item
                    if (marginBetweenItems > 0 && (state.itemCount > 1 || marginBottomOrRightOfLastItem)) {
                        outRect.bottom = marginBetweenItems
                    }
                }
                RecyclerView.HORIZONTAL -> {
                    //Update left of first item
                    val newStart = outRect.left + (if (marginTopOrLeftOfFirstItem) marginBetweenItems else 0)
                    if (newStart > 0) {
                        outRect.left = newStart
                    }
                    //Update right of first item
                    if (marginBetweenItems > 0) {
                        outRect.right = marginBetweenItems
                    }
                }
            }
            //Last item.
            (state.itemCount - 1) -> when (orientation) {
                RecyclerView.VERTICAL -> {
                    //Update bottom of lst item
                    if (marginBottomOrRightOfLastItem && marginBetweenItems > 0) {
                        outRect.bottom = marginBetweenItems
                    }
                }
                RecyclerView.HORIZONTAL -> {
                    //Update right of lst item
                    if (marginBottomOrRightOfLastItem && marginBetweenItems > 0) {
                        outRect.right = marginBetweenItems
                    }
                }
            }
            //Second item... N item.
            else -> when (orientation) {
                RecyclerView.VERTICAL -> if (marginBetweenItems > 0) {
                    outRect.bottom = marginBetweenItems
                }
                RecyclerView.HORIZONTAL -> if (marginBetweenItems > 0) {
                    outRect.right = marginBetweenItems
                }
            }
        }
    }
}