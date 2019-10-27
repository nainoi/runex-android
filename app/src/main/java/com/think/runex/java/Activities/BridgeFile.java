package com.think.runex.java.Activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.think.runex.R;
import com.think.runex.java.Constants.Constants;
import com.think.runex.java.Pages.MainPage;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.FragmentUtils;

public class BridgeFile extends FragmentActivity {
    /** Main variables */
    private final String ct = "BridgeFile->";


    // explicit variables
    private final int CONTAINER_ID = R.id.bridge_file_container;

    // views
    private BottomNavigationView bottomView;

    // on back pressed
    @Override
    public void onBackPressed() {
        if( FragmentUtils.getStackCount() <= 1 ) finish();
        else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.activity = this;
        setContentView(R.layout.activity_bridge_file);

        // fullscreen
        ActivityUtils.fullScreen();

        // view matching
        viewMatching(  );

        // Fragment inits
        FragmentUtils.containerId = CONTAINER_ID;
        FragmentUtils.activity = this;

        // display main page
        mainPage();

    }

    /** Feature methods */
    private void mainPage(){
        FragmentUtils.replaceFragment( new MainPage() );
    }

    /** View matching */
    private void viewMatching(){
        bottomView = findViewById(R.id.bottom_navigation);
    }
}
