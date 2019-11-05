package com.think.runex.java.Customize;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class xBrannerImage extends AppCompatImageView {
    public xBrannerImage(Context context) {
        super(context);
    }

    public xBrannerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xBrannerImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() / 2));
    }
}
