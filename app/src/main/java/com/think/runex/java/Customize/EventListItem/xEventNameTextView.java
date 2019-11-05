package com.think.runex.java.Customize.EventListItem;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.think.runex.java.Utils.DeviceUtils;

public class xEventNameTextView extends AppCompatTextView {
    private Point point;
    public xEventNameTextView(Context context) {
        super(context);
        init();
    }

    public xEventNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public xEventNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if( Constant.EVENT_NAME == -1 ) {
            point = DeviceUtils.instance(getContext()).getDisplaySize();
            Constant.EVENT_NAME = (int) ((point.x * 5.5) / 100);

        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, Constant.EVENT_NAME);
    }

}
