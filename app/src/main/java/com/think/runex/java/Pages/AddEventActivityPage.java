package com.think.runex.java.Pages;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Utils.ActivityUtils;

public class AddEventActivityPage extends xActivity {
    /**
     * Main variables
     */
    private final String ct = "AddEventActivityPage->";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_add_event_activity);

        ActivityUtils.newInstance(this).fullScreen();
    }
}
