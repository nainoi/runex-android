package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

public class xStatFrame extends LinearLayout {
    public xStatFrame(Context context) {
        super(context);
    }

    public xStatFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xStatFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),  (int)(getMeasuredWidth() / 2.5));
    }
}
