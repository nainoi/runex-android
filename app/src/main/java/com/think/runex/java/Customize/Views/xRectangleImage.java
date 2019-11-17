package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class xRectangleImage extends AppCompatImageView {
    public xRectangleImage(Context context) {
        super(context);
    }

    public xRectangleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xRectangleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth() / 1.5));
    }
}
