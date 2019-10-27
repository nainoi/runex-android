package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.think.runex.R;
import com.think.runex.java.Utils.StaticChildFragmentUtils;

public class MainPage extends Fragment {
    /** Main variables */
    private final String ct = "MainPage->";

    // explicit variables
    private final int CHILD_CONTAINER_ID = R.id.navigation_frame;
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

        return v;
    }

    /** Feature methods */
    private boolean updateScreen(int itemId){
        // prepare usage variables
        boolean onSelected = true;

        switch( itemId ){
            case R.id.menu_home: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new EventsPage()); break;
            case R.id.menu_my_events: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new MyEventPage()); break;
            case R.id.menu_record: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new RecordPage()); break;
            case R.id.menu_profile: StaticChildFragmentUtils.replaceChildFragment(CHILD_CONTAINER_ID, new ProfilePage()); break;
            default: onSelected = false; break;

        }

        return onSelected;
    }

    /** View event listener */
    private void viewEventListener(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        // update screen
        updateScreen(bottomNavigationView.getSelectedItemId());
    }
}
