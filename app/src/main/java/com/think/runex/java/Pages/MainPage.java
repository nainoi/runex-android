package com.think.runex.java.Pages;

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
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;

public class MainPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "MainPage->";

    // instance variables
    private xFragment pageRecord;
    private xFragment pageMyEvent;
    private xFragment pageProfile;

    // explicit variables
    private final int CHILD_CONTAINER_ID = R.id.navigation_frame;
    private int mCurrentItemId = -1;
    private Fragment mCurrentFragment = null;

    // views
    private BottomNavigationView bottomNavigationView;

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
                .hide(fragment).commit();

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

    private boolean updateScreen(int itemId) {
        // prepare usage variables
        boolean onSelected = true;

        // gone current displaying fragment
        if (mCurrentFragment != null) hideDisplayingFragment(mCurrentFragment);

        // prepare usage variables
        ChildFragmentUtils childFragmentUtils = ChildFragmentUtils.newInstance(this);

        switch (itemId) {
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
     * Matching view
     */
    private void matchingView(View v) {
        bottomNavigationView = v.findViewById(R.id.bottom_navigation);
    }

    /**
     * Life cycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // update props
        setChildContainerId(CHILD_CONTAINER_ID);

        // init pages
        pageMyEvent = new MyEventPage();
        pageRecord = new RecordPage().setPriority(Priority.PARENT).setFragmentHandler(new xFragmentHandler() {
            // prepare usage variables
            final String mtn = ct +"pageRecord() ";

            @Override
            public xFragment onResult(xTalk talk) {
                L.i(mtn +"xtalk request-code: "+ talk.requestCode);

                if (talk.requestCode == Globals.RC_TO_PROFILE_PAGE) {
                    // perform select
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);

                    // xtalk refresh
                    xTalk x = new xTalk();
                    x.requestCode = Globals.RC_REFRESH;

                    // refresh
                    pageProfile.onResult( x );

                }

                return null;
            }
        });
        pageProfile = new ProfilePage().setPriority(Priority.PARENT);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Globals.RC_RECORDER && resultCode == Activity.RESULT_OK) {
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        }
    }
}
