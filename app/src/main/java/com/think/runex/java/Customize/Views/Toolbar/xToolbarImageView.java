package com.think.runex.java.Customize.Views.Toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class xToolbarImageView extends AppCompatImageView {
    public xToolbarImageView(Context context) {
        super(context);
    }

    public xToolbarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xToolbarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void gone(){
        setVisibility(View.GONE);
    }
}
