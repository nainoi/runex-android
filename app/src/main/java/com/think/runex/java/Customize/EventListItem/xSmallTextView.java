package com.think.runex.java.Customize.EventListItem;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.think.runex.java.Utils.DeviceUtils;

public class xSmallTextView extends AppCompatTextView {
    private Point point;
    public xSmallTextView(Context context) {
        super(context);
        init();
    }

    public xSmallTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public xSmallTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if( Constant.SMALL_TEXT == -1 ) {
            point = DeviceUtils.instance(getContext()).getDisplaySize();
            Constant.SMALL_TEXT = (int)((point.x * 3.5) / 100);

        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, Constant.SMALL_TEXT);
    }

}
