package com.think.runex.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import com.think.runex.R

class CustomBottomBarBackground @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var path: Path? = null
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        color = Color.TRANSPARENT
    }

    @Dimension
    var centerIconSize: Int = 0

    init {
        attrs?.also {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.CustomBottomBarBackground)
            if (typeArray.hasValue(R.styleable.CustomBottomBarBackground_centerMenuSize)) {
                centerIconSize = typeArray.getDimension(R.styleable.CustomBottomBarBackground_centerMenuSize, 0f).toInt()
            }
            if (typeArray.hasValue(R.styleable.CustomBottomBarBackground_backgroundColor)) {
                paint.color = typeArray.getColor(R.styleable.CustomBottomBarBackground_backgroundColor, paint.color)
            }
            typeArray.recycle()
        }
    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        if (changed) {
//            calculateCenterIconPath(right - left, bottom - top)
//        }
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateCenterIconPath(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path?.also { canvas?.drawPath(it, paint) }
    }

    private fun calculateCenterIconPath(width: Int, height: Int) {

        if (centerIconSize <= 0) {
            path?.reset()
            path?.close()
            path = null
            return
        }

        val radius: Float = centerIconSize / 2f
        val centerHorizontal: Float = width / 2f
        val centerPoint = PointF(centerHorizontal, 0f)
        val centerBounds = RectF(centerPoint.x - radius, centerPoint.y - radius, centerPoint.x + radius, centerPoint.y + radius)

        path = Path()
        path?.moveTo(0f, 0f)
        path?.lineTo(centerBounds.left, 0f)
        path?.arcTo(centerBounds, 180f, -180f, false)
        path?.lineTo(width.toFloat(), 0f)
        path?.lineTo(width.toFloat(), height.toFloat())
        path?.lineTo(0f, height.toFloat())
        path?.close()
    }
}