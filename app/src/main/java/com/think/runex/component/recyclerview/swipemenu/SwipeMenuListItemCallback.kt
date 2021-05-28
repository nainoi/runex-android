package com.think.runex.component.recyclerview.swipemenu

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class SwipeMenuListItemCallback(
    private val menu: Collection<SwipeMenu>
) : ItemTouchHelper.Callback() {

    private var swipeFlags: Int = 0
    private var swipeState: Int = SwipeState.GONE
    private var isSwipeBack: Boolean = false
    private var menuWidth: Int = 420 //px
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var menuRectFList: ArrayList<RectF>? = null

    private var menuCountOnStart: Int = 0
    private var menuCountOnEnd: Int = 0

    init {

        //Count number of menu in start side and end side
        menuCountOnStart = 0
        menuCountOnEnd = 0

        //Set can swipe to start side or end side or both.
        var canSwipeStart = false
        var canSwipeEnd = false

        menu.forEach {
            if (it.gravity == Gravity.START) {
                canSwipeEnd = true
                menuCountOnStart++
            } else if (it.gravity == Gravity.END) {
                canSwipeStart = true
                menuCountOnEnd++
            }
        }

        if (canSwipeStart && canSwipeEnd) {
            swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        } else if (canSwipeStart) {
            swipeFlags = ItemTouchHelper.START
        } else if (canSwipeEnd) {
            swipeFlags = ItemTouchHelper.END
        }
    }

    private var onSwipeMenuSelected: ((position: Int, menu: SwipeMenu) -> Unit)? = null

    fun setOnSwipeMenuSelected(block: (position: Int, menu: SwipeMenu) -> Unit) {
        this.onSwipeMenuSelected = block
    }

    private var onTouchReleased: ((position: Int) -> Unit)? = null

    fun setOnTouchReleased(block: (position: Int) -> Unit) {
        this.onTouchReleased = block
    }

    fun setMenuWidth(menuWidth: Int) {
        this.menuWidth = menuWidth
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    //Disable Drag & Drop.
    override fun isLongPressDragEnabled(): Boolean = false

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (isSwipeBack) {
            isSwipeBack = swipeState != SwipeState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (swipeState != SwipeState.GONE) {
                val newDx = when (swipeState) {
                    SwipeState.START_VISIBLE -> max(dX, (menuWidth.toFloat() * menuCountOnStart))
                    SwipeState.END_VISIBLE -> min(dX, -(menuWidth.toFloat() * menuCountOnEnd))
                    else -> dX
                }
                super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        if (swipeState == SwipeState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        recyclerView.setOnTouchListener { _, event ->

            isSwipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP

            if (isSwipeBack) {

                if (dX > menuWidth) {
                    //Menu start visible
                    swipeState = SwipeState.START_VISIBLE
                } else if (dX < -menuWidth) {
                    //Menu end visible
                    swipeState = SwipeState.END_VISIBLE
                }

                if (swipeState != SwipeState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                } else {
                    swipeState = SwipeState.GONE
                    currentItemViewHolder = null
                    menuRectFList?.clear()
                    onTouchReleased?.invoke(viewHolder.adapterPosition)
                }
            }

            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {

                super.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { _, _ ->
                    false
                }

                setItemsClickable(recyclerView, true)
                isSwipeBack = false

                if (swipeState != SwipeState.GONE) {
                    if (menuRectFList != null && menuRectFList?.size == menu.size) {
                        menuRectFList?.forEachIndexed { index, menuRectF ->
                            if (menuRectF.contains(event.x, event.y)) {
                                onSwipeMenuSelected?.invoke(viewHolder.adapterPosition, menu.elementAt(index))
                            }
                        }
                    }
                }

                onTouchReleased?.invoke(viewHolder.adapterPosition)
                swipeState = SwipeState.GONE
                currentItemViewHolder = null
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawMenu(c: Canvas, viewHolder: RecyclerView.ViewHolder) {

        //Clear old menu rectF
        menuRectFList?.clear()
        menuRectFList = ArrayList<RectF>()

        val buttonWidthWithoutPadding = menuWidth - 1f
        var indexStart = 0
        var indexEnd = 0

        menu.forEach { swipeMenu ->

            var rectF: RectF? = null

            if (swipeMenu.gravity == Gravity.START) {
                val left = when (indexStart <= 0) {
                    true -> viewHolder.itemView.left.toFloat()
                    false -> viewHolder.itemView.left + (buttonWidthWithoutPadding * indexStart)
                }
                val right = when (indexStart <= 0) {
                    true -> viewHolder.itemView.left + buttonWidthWithoutPadding
                    false -> viewHolder.itemView.left + (buttonWidthWithoutPadding * (indexStart + 1))
                }

                rectF = RectF(left, viewHolder.itemView.top.toFloat(), right, viewHolder.itemView.bottom.toFloat())
                indexStart++

            } else if (swipeMenu.gravity == Gravity.END) {

                val left = when (indexEnd <= 0) {
                    true -> viewHolder.itemView.right - buttonWidthWithoutPadding
                    false -> viewHolder.itemView.right - (buttonWidthWithoutPadding * (indexEnd + 1))
                }
                val right = when (indexEnd <= 0) {
                    true -> viewHolder.itemView.right.toFloat()
                    false -> viewHolder.itemView.right - (buttonWidthWithoutPadding * indexEnd)
                }

                rectF = RectF(left, viewHolder.itemView.top.toFloat(), right, viewHolder.itemView.bottom.toFloat())
                indexEnd++
            }

            if (rectF != null) {
                menuRectFList?.add(rectF)
                val paint = Paint()

                //Draw layout
                paint.color = swipeMenu.backgroundColor
                c.drawRect(rectF, paint)

                //Draw text
                paint.color = Color.WHITE
                paint.isAntiAlias = true
                paint.textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    14f,
                    viewHolder.itemView.resources.displayMetrics
                )
                val textWidth = paint.measureText(swipeMenu.name)
                c.drawText(
                    swipeMenu.name,
                    rectF.centerX() - (textWidth / 2),
                    rectF.centerY() + (paint.textSize / 2),
                    paint
                )
            }
        }
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {
            drawMenu(c, it)
        }
    }
}