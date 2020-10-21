package com.think.runex.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.think.runex.R
import kotlin.math.min

class AspectRatioImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {


    private var ratioWidth: Float = 1f
    private var ratioHeight: Float = 1f

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
            try {
                if (a.hasValue(R.styleable.AspectRatioImageView_ratioWidth)) {
                    ratioWidth = a.getFloat(R.styleable.AspectRatioImageView_ratioWidth, 1f)
                }
                if (a.hasValue(R.styleable.AspectRatioImageView_ratioHeight)) {
                    ratioHeight = a.getFloat(R.styleable.AspectRatioImageView_ratioHeight, 1f)
                }
            } finally {
                a.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSize: Int = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize: Int = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSize == 0 && heightSize == 0) {
            // If there are no constraints on size, let FrameLayout measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

            // Now use the smallest of the measured dimensions for both dimensions
            val minSize = min(measuredWidth, measuredHeight)
            setMeasuredDimension(minSize, minSize)
        } else {
            if (ratioWidth >= ratioHeight) {
                val factor = (widthSize / ratioWidth).toInt()
                heightSize = (factor * ratioHeight).toInt()
            } else {
                val factor = (heightSize / ratioHeight).toInt()
                widthSize = (factor * ratioWidth).toInt()
            }
            setMeasuredDimension(widthSize, heightSize)
        }
    }
}