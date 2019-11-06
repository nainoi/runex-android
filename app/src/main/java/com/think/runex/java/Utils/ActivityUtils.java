package com.think.runex.java.Utils;

import android.app.Activity;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class ActivityUtils {

    private FragmentActivity activity;

    private ActivityUtils(FragmentActivity activity){
        this.activity = activity;
    }
    public static ActivityUtils newInstance(FragmentActivity activity){
        return new ActivityUtils( activity );
    }

    public void fullScreen(){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
