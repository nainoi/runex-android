package com.think.runex.java.Customize.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.think.runex.java.Utils.DeviceUtils;

public class xRectangleViewGroup extends FrameLayout {

    private int width = 1;

    public xRectangleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public xRectangleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        int dWidth = DeviceUtils.instance(getContext()).getDisplaySize().x;
//        dWidth =  (dWidth == 0) ? 1024 : 0;

        width = (int)(dWidth / 3.5);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, (int)(width / 1.5));
    }
}
