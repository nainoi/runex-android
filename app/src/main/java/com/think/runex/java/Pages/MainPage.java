package com.think.runex.java.Pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.think.runex.R;
import com.think.runex.java.Activities._RecordActivity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.Priority;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Fragment.xFragmentHandler;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Pages.RecordPage.v2.RecordPage;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;

public class MainPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "MainPage->";

    // instance variables
    private xFragment pageAllEvent;
    private xFragment pageMyEvent;
    private xFragment pageRecord;
    private xFragment pageProfile;

    // explicit variables
    private final int CHILD_CONTAINER_ID = R.id.navigation_frame;
    private int mCurrentItemId = -1;
    private Fragment mCurrentFragment = null;
    //--> pages
    private final String PAGE_RECORD = "PAGE_RECORD";
    private final String PAGE_PROFILE = "PAGE_PROFILE";
    private final String PAGE_MY_EVENT = "PAGE_MY_EVENT";

    // views
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // update props
        setChildContainerId(CHILD_CONTAINER_ID);

        if (savedInstanceState == null) {
            // init pages
            pageAllEvent = new AllEventsPage();
            pageMyEvent = new MyEventPage();
            pageRecord = getRecordPage();
            pageProfile = new ProfilePage().setPriority(Priority.PARENT);

        } else {
            if ((pageRecord = (xFragment) getChildFragmentManager().getFragment(savedInstanceState, PAGE_RECORD)) == null) {
                pageRecord = getRecordPage();

            }

            if ((pageMyEvent = (xFragment) getChildFragmentManager().getFragment(savedInstanceState, PAGE_MY_EVENT)) == null) {
                pageMyEvent = new MyEventPage();

            }

            if ((pageProfile = (xFragment) getChildFragmentManager().getFragment(savedInstanceState, PAGE_PROFILE)) == null) {
                pageProfile = new ProfilePage().setPriority(Priority.PARENT);

            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_main, container, false);

        // matching view
        matchingView(v);

        // view event listener
        viewEventListener();

        // custom bottom navigation item
        customBottomNavMenu();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // same position
        if (bottomNavigationView.getSelectedItemId() == mCurrentItemId) return;

        // update screen
        updateScreen(bottomNavigationView.getSelectedItemId());
//
//        // perform select
//        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
    }

    /**
     * Matching view
     */
    private void matchingView(View v) {
        bottomNavigationView = v.findViewById(R.id.bottom_navigation);
    }


    /**
     * Feature methods
     */
    private void hideDisplayingFragment(Fragment fragment) {
        // prepare usage variables
        final String mtn = ct + "hideDisplayingFragment() ";

        // log
        L.i(mtn + "Hide fragment: " + fragment.getClass().getSimpleName());

        // gone current child
        getChildFragmentManager().beginTransaction()
                .hide(fragment).commitAllowingStateLoss();

    }

    private void customBottomNavMenu() {
        // prepare usage variables
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            // prepare usage variables
            final View icon = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            // update layout params
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);

            // update layout params
            icon.setLayoutParams(layoutParams);

        }
    }

    private void recordPage() {
        Intent i = new Intent(activity, _RecordActivity.class);
        startActivityForResult(i, Globals.RC_RECORDER);

    }

    @SuppressLint("NonConstantResourceId")
    private boolean updateScreen(int itemId) {
        // prepare usage variables
        boolean onSelected = true;

        // gone current displaying fragment
        if (mCurrentFragment != null) hideDisplayingFragment(mCurrentFragment);

        // prepare usage variables
        ChildFragmentUtils childFragmentUtils = ChildFragmentUtils.newInstance(this);

        switch (itemId) {
            case R.id.menu_home:
                childFragmentUtils.addChildFragment(CHILD_CONTAINER_ID, pageAllEvent);
                mCurrentFragment = pageAllEvent;
                break;
            case R.id.menu_my_events:
                childFragmentUtils.addChildFragment(CHILD_CONTAINER_ID, pageMyEvent);
                mCurrentFragment = pageMyEvent;

                // feature
                pageMyEvent.onResult(null);

                break;
            case R.id.menu_record:
                childFragmentUtils.addChildFragment(CHILD_CONTAINER_ID, pageRecord);
                mCurrentFragment = pageRecord;
                break;
            case R.id.menu_profile:
                childFragmentUtils.addChildFragment(CHILD_CONTAINER_ID, pageProfile);
                mCurrentFragment = pageProfile;
                break;
//            default: onSelected = false; break;

        }

        // update current item id
        if (onSelected) {
            mCurrentItemId = itemId;
        }

        return onSelected;
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // click on same button
                if (item.getItemId() == mCurrentItemId) return false;

                return updateScreen(item.getItemId());
            }
        });
    }

    /**
     * Getter
     */
    private xFragment getRecordPage() {
        return new RecordPage().setPriority(Priority.PARENT).setFragmentHandler(new xFragmentHandler() {
            // prepare usage variables
            final String mtn = ct + "pageRecord() ";

            @Override
            public xFragment onResult(xTalk talk) {
                L.i(mtn + "xtalk request-code: " + talk.requestCode);

                if (talk.requestCode == Globals.RC_TO_PROFILE_PAGE) {
                    // perform select
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);

                    // xtalk refresh
                    xTalk x = new xTalk();
                    x.requestCode = Globals.RC_REFRESH;

                    // refresh
                    pageProfile.onResult(x);

                }

                return null;
            }
        });
    }

    /**
     * Life cycle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        if(pageRecord.isAdded()) getChildFragmentManager().putFragment(outState, PAGE_RECORD, pageRecord);
//        if(pageProfile.isAdded()) getChildFragmentManager().putFragment(outState, PAGE_PROFILE, pageProfile);
//        if(pageMyEvent.isAdded()) getChildFragmentManager().putFragment(outState, PAGE_MY_EVENT, pageMyEvent);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Globals.RC_RECORDER && resultCode == Activity.RESULT_OK) {
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        }
    }
}
