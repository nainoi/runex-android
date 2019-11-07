package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.DeviceUtils;

public class xProfileImage extends AppCompatImageView {
    public xProfileImage(Context context) {
        super(context);
    }

    public xProfileImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xProfileImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Point point = DeviceUtils.instance(getContext()).getDisplaySize();
        final int w = (point.x == 0 ? Globals.FIXED_SCREEN_WIDTH  / 8  : getMeasuredHeight());

        setMeasuredDimension(w, w);
    }
}
