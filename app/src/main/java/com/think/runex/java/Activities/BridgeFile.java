package com.think.runex.java.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
    private final String FRAGMENT_MAIN_PAGE = "MAIN_PAGE";

    // views
    private BottomNavigationView bottomView;

    // on back pressed
    @Override
    public void onBackPressed() {
        // prepare usage variables
        final String mtn = ct +"onBackPressed() ";

        if( preventFromBackPressed ) return;

        // support back stack
        if( getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            // pop back stack out
            getSupportFragmentManager().popBackStackImmediate();

            // exit from this process
            return;
        }

        // prepare usage variables
        final xFragment fragment = (xFragment)getSupportFragmentManager().findFragmentById(CONTAINER_ID);

        // default
        if( fragment == null ) {
            // default inherit methods
            super.onBackPressed();

            // exit from this process
            return;
        }

        // prepare usage variables
        xFragment displayedFragment = mMainPage.getCurrentDisplayedFragment("BridgeFile", mMainPage );

        // remove current displayed fragment
        if( displayedFragment != null ) {
            // on back pressed
            if(!mMainPage.onBackPressed( displayedFragment )) {
                // default back pressed method
                super.onBackPressed();

                // exit from this process
                return;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_file);
        // prepare usage variables
        final String mtn = ct +"onCreate() ";

        // fullscreen
        ActivityUtils uts = ActivityUtils.newInstance(this);
        uts.fullScreen();

        // update container id
        setFragmentContainerId( CONTAINER_ID );

        // view matching
        viewMatching();

        // Fragment inits
        mFragmentUtils = FragmentUtils.newInstance(this, CONTAINER_ID);

        if( savedInstanceState == null ) {
            // create fragment
            mMainPage = new MainPage().setPriority(Priority.PARENT);

            // display main page
            mainPage();

        } else {
            // state fragment
            mMainPage = (xFragment)getSupportFragmentManager().getFragment(savedInstanceState,  FRAGMENT_MAIN_PAGE);

//            Toast.makeText(this, mtn +"isAdd: "+ mMainPage.isAdded(), Toast.LENGTH_SHORT).show();

            // display stated fragment
            getSupportFragmentManager().beginTransaction()
                    .show( mMainPage )
                    .commit();

        }

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
    protected void onSaveInstanceState(Bundle outState) {

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, FRAGMENT_MAIN_PAGE, mMainPage);

        super.onSaveInstanceState(outState);

    }    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";
    }
}
