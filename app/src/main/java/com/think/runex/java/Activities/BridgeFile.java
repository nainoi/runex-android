package com.think.runex.java.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.think.runex.R;
import com.think.runex.java.Constants.Constants;
import com.think.runex.java.Constants.Priority;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Pages.EventDetailPage;
import com.think.runex.java.Pages.MainPage;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;

import java.util.List;

public class BridgeFile extends xActivity {
    /**
     * Main variables
     */
    private final String ct = "BridgeFile->";

    // instance variables
    private FragmentUtils mFragmentUtils;
    private xFragment mMainPage;

    // explicit variables
    private final int CONTAINER_ID = R.id.bridge_file_container;

    // views
    private BottomNavigationView bottomView;

    // on back pressed
    @Override
    public void onBackPressed() {
        // prepare usage variables
        final String mtn = ct +"onBackPressed() ";
        final xFragment fragment = (xFragment)getSupportFragmentManager().findFragmentById(CONTAINER_ID);

        if( fragment == null ) super.onBackPressed();

        xFragment displayedFragment = mMainPage.getCurrentDisplayedFragment("BridgeFile", mMainPage );
        if( displayedFragment == null ) L.i(mtn +"displayedFragment: null");
        else L.i(mtn +"displayedFragment: "+ displayedFragment.getClass().getSimpleName());

        if( displayedFragment != null ) {
            L.i(mtn +"displayedFragment: "+ displayedFragment.getClass().getSimpleName());
            // on back pressed
            if(!mMainPage.onBackPressed( displayedFragment )){
                L.i(mtn +"use super.onBackPressed");
                super.onBackPressed();

            }

        } else L.i(mtn +"displayedFragment is not ready. ");
//        L.i(" ");
//        L.i(" ");
//        L.i(" ");

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_file);

        // fullscreen
        ActivityUtils uts = ActivityUtils.newInstance(this);
        uts.fullScreen();


        // view matching
        viewMatching();

        // Fragment inits
        mFragmentUtils = FragmentUtils.newInstance(this, CONTAINER_ID);
        mMainPage =  new MainPage().setPriority(Priority.PARENT);

        // display main page
        mainPage();

    }

    /**
     * Feature methods
     */
    private void mainPage() {
        mFragmentUtils.addFragment(CONTAINER_ID, mMainPage);
    }

    /**
     * View matching
     */
    private void viewMatching() {
        bottomView = findViewById(R.id.bottom_navigation);
    }

    /**
     * Life cycle
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";
    }
}
