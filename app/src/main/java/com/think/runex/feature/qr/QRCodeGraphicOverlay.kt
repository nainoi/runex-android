package com.think.runex.feature.qr

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.annotation.Px
import com.jozzee.android.core.dimension.dpToPx
import com.think.runex.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class QRCodeGraphicOverlay @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_FRAME_ASPECT_RATIO_WIDTH = 1f
        private const val DEFAULT_FRAME_ASPECT_RATIO_HEIGHT = 1f

        private const val DEFAULT_FRAME_CORNER_LENGTH_DP = 32
        private const val DEFAULT_FRAME_CORNER_THICKNESS_DP = 4f
        private const val DEFAULT_FRAME_COLOR = Color.WHITE
        private const val DEFAULT_FRAME_SIZE = 0.75f
        private const val DEFAULT_MASK_COLOR = 0x77000000
    }

    private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val framePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val path: Path = Path().apply {
        fillType = Path.FillType.EVEN_ODD
    }

    lateinit var frameRect: Rect
        private set


    /**
     * Percent size of frame 0 - 1
     */
    @FloatRange(from = 0.1, to = 1.0)
    var frameSizePercent = 0.75f
        set(value) {
            field = value
            invalidateFrameRect()
            if (isLaidOut) {
                invalidate()
            }
        }

    /**
     * Length in each corner at px.
     */
    @Px
    var frameCornersLength: Int = 0
        set(value) {
            field = value
            if (isLaidOut) {
                invalidate()
            }
        }

    /**
     * Thickness of frame corners.
     */
    @Px
    var frameCornersThickness: Float = 0f
        set(value) {
            field = value
            framePaint.strokeWidth = field
            if (isLaidOut) {
                invalidate()
            }
        }

    /**
     * Radius in each corner at px.
     */
    @Px
    var frameCornersRadius: Int = 0
        set(value) {
            field = value
            if (isLaidOut) {
                invalidate()
            }
        }

    @ColorInt
    var frameColor: Int = DEFAULT_FRAME_COLOR
        set(value) {
            field = value
            framePaint.color = field
            if (isLaidOut) {
                invalidate()
            }
        }

    @ColorInt
    var maskColor: Int = DEFAULT_MASK_COLOR
        set(value) {
            field = value
            maskPaint.color = field
            if (isLaidOut) {
                invalidate()
            }
        }


    @FloatRange(from = 0.0, fromInclusive = false)
    private var frameRatioWidth = 1f
        set(value) {
            field = value
            if (isLaidOut) {
                invalidate()
            }
        }

    @FloatRange(from = 0.0, fromInclusive = false)
    private var frameRatioHeight = 1f
        set(value) {
            field = value
            if (isLaidOut) {
                invalidate()
            }
        }

    @Dimension
    private var frameMarginTop: Float = 0f
        set(value) {
            field = value
            if (isLaidOut) {
                invalidate()
            }
        }

    init {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {

        //Initial frame.
        setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH, DEFAULT_FRAME_ASPECT_RATIO_HEIGHT)
        frameSizePercent = DEFAULT_FRAME_SIZE
        frameCornersLength = dpToPx(DEFAULT_FRAME_CORNER_LENGTH_DP)
        frameCornersThickness = dpToPx(DEFAULT_FRAME_CORNER_THICKNESS_DP)
        frameColor = DEFAULT_FRAME_COLOR
        maskColor = DEFAULT_MASK_COLOR

        attrs?.also {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.QRGraphicOverlay)

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameAspectRatioWidth)) {
                frameRatioWidth = typeArray.getFloat(R.styleable.QRGraphicOverlay_frameAspectRatioWidth, DEFAULT_FRAME_ASPECT_RATIO_WIDTH)
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameAspectRatioHeight)) {
                frameRatioHeight = typeArray.getFloat(R.styleable.QRGraphicOverlay_frameAspectRatioHeight, DEFAULT_FRAME_ASPECT_RATIO_HEIGHT)
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameSizePercent)) {
                val percent = typeArray.getFloat(R.styleable.QRGraphicOverlay_frameSizePercent, DEFAULT_FRAME_SIZE)
                if (percent > 0 && percent <= 1) {
                    frameSizePercent = percent
                }
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameCornersLength)) {
                frameCornersLength = typeArray.getDimension(
                        R.styleable.QRGraphicOverlay_frameCornersLength,
                        DEFAULT_FRAME_CORNER_LENGTH_DP.toFloat()).toInt()
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameCornersThickness)) {
                frameCornersThickness = typeArray.getDimension(
                        R.styleable.QRGraphicOverlay_frameCornersThickness,
                        DEFAULT_FRAME_CORNER_THICKNESS_DP)
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameCornersRadius)) {
                frameCornersRadius = typeArray.getDimension(R.styleable.QRGraphicOverlay_frameCornersRadius, 0f).toInt()
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameColor)) {
                frameColor = typeArray.getColor(R.styleable.QRGraphicOverlay_frameColor, DEFAULT_FRAME_COLOR)
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_maskColor)) {
                maskColor = typeArray.getColor(R.styleable.QRGraphicOverlay_maskColor, DEFAULT_MASK_COLOR)
            }

            if (typeArray.hasValue(R.styleable.QRGraphicOverlay_frameMarginTop)) {
                frameMarginTop = typeArray.getDimension(R.styleable.QRGraphicOverlay_frameMarginTop, 0f)
            }

            typeArray.recycle()
        }
    }

    /**
     * Adjusts the `rect`'s coordinate from the preview's coordinate system to the view
     * coordinate system.
     */
    fun translateRect(imageRect: Rect, boundingBox: Rect): Rect {

        // Picks the barcode, if exists, that covers the center of graphic overlay.
        val widthScaleFactor = width.toFloat() / imageRect.height()
        val heightScaleFactor = height.toFloat() / imageRect.width()

        return Rect((boundingBox.left * widthScaleFactor).toInt(),
                (boundingBox.top * heightScaleFactor).toInt(),
                (boundingBox.right * widthScaleFactor).toInt(),
                (boundingBox.bottom * heightScaleFactor).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (::frameRect.isInitialized.not()) return

        val top: Float = frameRect.top.toFloat()
        val left: Float = frameRect.left.toFloat()
        val right: Float = frameRect.right.toFloat()
        val bottom: Float = frameRect.bottom.toFloat()

        when (frameCornersRadius > 0) {
            true -> {
                val normalizedRadius = min(frameCornersRadius, max(frameCornersLength - 1, 0))
                path.reset()
                path.moveTo(left, top + normalizedRadius)
                path.quadTo(left, top, left + normalizedRadius, top)
                path.lineTo(right - normalizedRadius, top)
                path.quadTo(right, top, right, top + normalizedRadius)
                path.lineTo(right, bottom - normalizedRadius)
                path.quadTo(right, bottom, right - normalizedRadius, bottom)
                path.lineTo(left + normalizedRadius, bottom)
                path.quadTo(left, bottom, left, bottom - normalizedRadius)
                path.lineTo(left, top + normalizedRadius)
                path.moveTo(0f, 0f)
                path.lineTo(width.toFloat(), 0f)
                path.lineTo(width.toFloat(), height.toFloat())
                path.lineTo(0f, height.toFloat())
                path.lineTo(0f, 0f)
                canvas.drawPath(path, maskPaint)
                path.reset()
                path.moveTo(left, top + frameCornersLength)
                path.lineTo(left, top + normalizedRadius)
                path.quadTo(left, top, left + normalizedRadius, top)
                path.lineTo(left + frameCornersLength, top)
                path.moveTo(right - frameCornersLength, top)
                path.lineTo(right - normalizedRadius, top)
                path.quadTo(right, top, right, top + normalizedRadius)
                path.lineTo(right, top + frameCornersLength)
                path.moveTo(right, bottom - frameCornersLength)
                path.lineTo(right, bottom - normalizedRadius)
                path.quadTo(right, bottom, right - normalizedRadius, bottom)
                path.lineTo(right - frameCornersLength, bottom)
                path.moveTo(left + frameCornersLength, bottom)
                path.lineTo(left + normalizedRadius, bottom)
                path.quadTo(left, bottom, left, bottom - normalizedRadius)
                path.lineTo(left, bottom - frameCornersLength)
                canvas.drawPath(path, framePaint)
            }
            false -> {
                path.reset()
                path.moveTo(left, top)
                path.lineTo(right, top)
                path.lineTo(right, bottom)
                path.lineTo(left, bottom)
                path.lineTo(left, top)
                path.moveTo(0f, 0f)
                path.lineTo(width.toFloat(), 0f)
                path.lineTo(width.toFloat(), height.toFloat())
                path.lineTo(0f, height.toFloat())
                path.lineTo(0f, 0f)
                canvas.drawPath(path, maskPaint)
                path.reset()
                path.moveTo(left, top + frameCornersLength)
                path.lineTo(left, top)
                path.lineTo(left + frameCornersLength, top)
                path.moveTo(right - frameCornersLength, top)
                path.lineTo(right, top)
                path.lineTo(right, top + frameCornersLength)
                path.moveTo(right, bottom - frameCornersLength)
                path.lineTo(right, bottom)
                path.lineTo(right - frameCornersLength, bottom)
                path.moveTo(left + frameCornersLength, bottom)
                path.lineTo(left, bottom)
                path.lineTo(left, bottom - frameCornersLength)
                canvas.drawPath(path, framePaint)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        invalidateFrameRect(right - left, bottom - top)
    }

    fun setFrameAspectRatio(@FloatRange(from = 0.0, fromInclusive = false) ratioWidth: Float,
                            @FloatRange(from = 0.0, fromInclusive = false) ratioHeight: Float) {
        frameRatioWidth = ratioWidth
        frameRatioHeight = ratioHeight
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    private fun invalidateFrameRect() {
        invalidateFrameRect(width, height)
    }

    private fun invalidateFrameRect(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            val viewAR: Float = width.toFloat() / height.toFloat()
            val frameAR: Float = frameRatioWidth / frameRatioHeight

            var frameWidth = 0
            var frameHeight = 0
            when (viewAR <= frameAR) {
                true -> {
                    frameWidth = (width * frameSizePercent).roundToInt()
                    frameHeight = (frameWidth / frameAR).roundToInt()
                }
                false -> {
                    frameHeight = (height * frameSizePercent).roundToInt()
                    frameWidth = (frameHeight / frameAR).roundToInt()
                }
            }
            val frameLeft = (width - frameWidth) / 2
            val frameTop = (((height - frameHeight) / 2) + frameMarginTop).toInt()
            frameRect = Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight)
        }
    }
}