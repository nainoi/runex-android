package com.think.runex.component

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import com.jozzee.android.core.view.warpContent
import com.think.runex.R
import com.think.runex.util.extension.setColorFilter

class ProgressLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var progressBar: ProgressBar

    @ColorInt
    var progressColor: Int = -1
        set(value) {
            field = value
            progressBar.indeterminateDrawable.setColorFilter(field)
        }

    init {
        createViews()
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressLayout)

            if (typedArray.hasValue(R.styleable.ProgressLayout_progressColor)) {
                progressColor = typedArray.getColor(R.styleable.ProgressLayout_progressColor, -1)
            }

            typedArray.recycle()
        }
    }

    private fun createViews() {
        setOnClickListener {

        }

        progressBar = ProgressBar(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(warpContent(), warpContent()).apply {
                gravity = Gravity.CENTER
            }
        }
        addView(progressBar)
    }
}