package com.think.runex.java.Customize.Login;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.think.runex.java.Utils.DeviceUtils;

public class xSocialTextView extends AppCompatTextView {
    private Point point;
    public xSocialTextView(Context context) {
        super(context);
        init();
    }

    public xSocialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public xSocialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        point = DeviceUtils.instance( getContext() ).getDisplaySize();
        final int size = (int)((point.x * 3.5) / 100);

        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

}
