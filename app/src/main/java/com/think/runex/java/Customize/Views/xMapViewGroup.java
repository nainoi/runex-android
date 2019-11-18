package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.java.Utils.DeviceUtils;

public class xMapViewGroup extends FrameLayout {
    private final double DEFAULT_WIDTH_DIVIDER = 1.1;
    private double divider = DEFAULT_WIDTH_DIVIDER;
    public xMapViewGroup(@NonNull Context context) {
        super(context);
    }

    public xMapViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public xMapViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void overrideRequestLayout(double divider){
        // update divider
        this.divider = divider;

        // on measure
        measure( getMeasuredWidth(), getMeasuredWidth());

        // request layout
        requestLayout();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth() / divider));
    }
}
