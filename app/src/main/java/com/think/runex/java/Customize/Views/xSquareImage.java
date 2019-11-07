package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class xSquareImage extends AppCompatImageView {
    public xSquareImage(Context context) {
        super(context);
    }

    public xSquareImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xSquareImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
