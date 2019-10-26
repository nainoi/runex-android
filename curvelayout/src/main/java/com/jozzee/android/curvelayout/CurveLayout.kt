package com.jozzee.android.curvelayout

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

class CurveLayout @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var path: Path = Path()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var pdMode: PorterDuffXfermode
    private var mask: Bitmap? = null

    /**
     * Height of curves in pixel.
     */
    var curvesHeight: Int = 0
        set(value) {
            field = value
            calculateCurve()
        }

    @CurveFormat
    var curvesFormat: Int = CurveFormat.CONVEX
        set(value) {
            field = value
            calculateCurve()
        }

    @CurveGravity
    var curvesGravity: Int = CurveGravity.BOTTOM
        set(value) {
            field = value
            calculateCurve()
        }

    init {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 1f
        pdMode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        //TODO("Set [isDrawingCacheEnabled] for preview in Android studio")
        isDrawingCacheEnabled = true
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)

        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CurveLayout)

            if (typeArray.hasValue(R.styleable.CurveLayout_curveHeight)) {
                curvesHeight = typeArray.getDimension(R.styleable.CurveLayout_curveHeight, 16f).toInt()
            }

            if (typeArray.hasValue(R.styleable.CurveLayout_curveFormat)) {
                curvesFormat = typeArray.getInt(R.styleable.CurveLayout_curveFormat, CurveFormat.CONVEX)
            }

            if (typeArray.hasValue(R.styleable.CurveLayout_curvesGravity)) {
                curvesGravity = typeArray.getInt(R.styleable.CurveLayout_curvesGravity, CurveGravity.BOTTOM)
            }

            typeArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            calculateCurve()
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        mask?.also {
            paint.xfermode = pdMode
            canvas?.drawBitmap(it, 0f, 0f, paint)
            paint.xfermode = null
        }
    }

    override fun setBackground(background: Drawable?) {
        //disabled set a background, please set a background to view child.
        //super.setBackground(background)
    }

    override fun setBackgroundResource(resid: Int) {
        //disabled set a background, please set a background to view child.
        //super.setBackgroundResource(resid)
    }

    override fun setBackgroundColor(color: Int) {
        //disabled set a background, please set a background to view child.
        //super.setBackgroundColor(color)
    }

    private fun calculateCurve() {
        val width: Float = measuredWidth.toFloat()
        val height: Float = measuredHeight.toFloat()

        if (width > 0 || height > 0) {
            if (mask != null && mask?.isRecycled != true) {
                mask?.recycle()
            }

            val newPath = Path()
            when (curvesGravity) {
                CurveGravity.BOTTOM -> when (curvesFormat) {
                    CurveFormat.CONCAVE -> {
                        newPath.moveTo(0f, 0f)
                        newPath.lineTo(0f, height)
                        newPath.quadTo(width / 2, height - 2 * curvesHeight, width, height)
                        newPath.lineTo(width, 0f)
                        newPath.close()
                    }
                    CurveFormat.CONVEX -> {
                        newPath.moveTo(0f, 0f)
                        newPath.lineTo(0f, height - curvesHeight)
                        newPath.quadTo(width / 2, height + curvesHeight, width, height - curvesHeight)
                        newPath.lineTo(width, 0f)
                        newPath.close()
                    }
                }
                CurveGravity.TOP -> when (curvesFormat) {
                    CurveFormat.CONCAVE -> {
                        newPath.moveTo(0f, height)
                        newPath.lineTo(0f, 0f)
                        newPath.quadTo(width / 2, 2f * curvesHeight, width, 0f)
                        newPath.lineTo(width, height)
                        newPath.close()
                    }
                    CurveFormat.CONVEX -> {
                        newPath.moveTo(0f, curvesHeight.toFloat())
                        newPath.quadTo(width / 2, -curvesHeight.toFloat(), width, curvesHeight.toFloat())
                        newPath.lineTo(width, height)
                        newPath.lineTo(0f, height)
                        newPath.close()
                    }
                }
                CurveGravity.LEFT -> when (curvesFormat) {
                    CurveFormat.CONCAVE -> {
                        newPath.moveTo(width, 0f)
                        newPath.lineTo(0f, 0f)
                        newPath.quadTo(curvesHeight * 2f, height / 2, 0f, height)
                        newPath.lineTo(width, height)
                        newPath.close()
                    }
                    CurveFormat.CONVEX -> {
                        newPath.moveTo(width, 0f)
                        newPath.lineTo(curvesHeight.toFloat(), 0f)
                        newPath.quadTo(-curvesHeight.toFloat(), height / 2, curvesHeight.toFloat(), height)
                        newPath.lineTo(width, height)
                        newPath.close()
                    }
                }
                CurveGravity.RIGHT -> when (curvesFormat) {
                    CurveFormat.CONCAVE -> {
                        newPath.moveTo(0f, 0f)
                        newPath.lineTo(width, 0f)
                        newPath.quadTo(width - curvesHeight.toFloat() * 2, height / 2, width, height)
                        newPath.lineTo(0f, height)
                        newPath.close()
                    }
                    CurveFormat.CONVEX -> {
                        newPath.moveTo(0f, 0f)
                        newPath.lineTo(width - curvesHeight.toFloat(), 0f)
                        newPath.quadTo(width + curvesHeight, height / 2, width - curvesHeight, height)
                        newPath.lineTo(0f, height)
                        newPath.close()
                    }
                }
            }

            path.reset()
            path.set(newPath)
            mask = createMask(width.toInt(), height.toInt())

            if (ViewCompat.getElevation(this) > 0f) {
                try {
                    outlineProvider = getOutlineProviderElevation()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        postInvalidate()
    }

    private fun createMask(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPath(path, paint)
        return bitmap
    }

    private fun getOutlineProviderElevation(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                try {
                    outline?.setConvexPath(path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        if (mask != null && mask?.isRecycled != true) {
            mask?.recycle()
            mask = null
        }
        super.onDetachedFromWindow()
    }

}