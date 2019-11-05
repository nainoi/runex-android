package com.think.runex.java.Pages;

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
import com.think.runex.java.Activities.RecordActivity;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Utils.StaticChildFragmentUtils;

public class MainPage extends xFragment {
    /** Main variables */
    private final String ct = "MainPage->";

    // explicit variables
    private final int CHILD_CONTAINER_ID = R.id.navigation_frame;
    private int mCurrentItemId = -1;

    // views
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_main, container, false);

        // init child fragment
        StaticChildFragmentUtils.childFragmentManager = getChildFragmentManager();

        // matching view
        matchingView( v );

        // view event listener
        viewEventListener();

        // custom bottom navigation item
        customBottomNavMenu();

        return v;
    }

    /** Feature methods */
    private void customBottomNavMenu(){
        // prepare usage variables
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            // prepare usage variables
            final View icon = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            // update layout params
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, displayMetrics);

            // update layout params
            icon.setLayoutParams(layoutParams);

        }
    }
    private void recordPage(){
        Intent i = new Intent(activity, RecordActivity.class);
        startActivityForResult(i, 0);

    }
    private boolean updateScreen(int itemId){
        // prepare usage variables
        boolean onSelected = true;

        switch( itemId ){
            case R.id.menu_home: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new EventsPage()); break;
            case R.id.menu_my_events: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new MyEventPage()); break;
            case R.id.menu_record: recordPage(); break;// StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new RecordPage()); break;
            case R.id.menu_profile: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new ProfilePage()); break;
            default: onSelected = false; break;

        }

        // update current item id
        if( onSelected ) mCurrentItemId = itemId;

        return onSelected;
    }

    /** View event listener */
    private void viewEventListener(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if( item.getItemId() == mCurrentItemId ) return false;
                if( item.getItemId() == R.id.menu_record ) {
                    // display record page
                    recordPage();

                    // exit from this process
                    return false;
                }

                return updateScreen( item.getItemId() );
            }
        });
    }

    /** Matching view */
    private void matchingView( View v ){
        bottomNavigationView = v.findViewById(R.id.bottom_navigation);
    }

    /** Life cycle */
    @Override
    public void onResume() {
        super.onResume();

        // same position
        if( bottomNavigationView.getSelectedItemId() == mCurrentItemId ) return;

        // update screen
        updateScreen(bottomNavigationView.getSelectedItemId());
    }
}
