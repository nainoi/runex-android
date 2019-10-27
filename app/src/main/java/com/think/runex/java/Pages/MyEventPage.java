package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.Pages.Summary.SummaryPage;
import com.think.runex.java.Utils.ChildFragmentUtils;

public class MyEventPage extends Fragment {

    /** Main variables */
    private final String ct = "MyEventPage->";

    // instance variables
    private ChildFragmentUtils mChildFragmentUtils;

    // explicit variables
    private final int SUMMARY_PAGE_CONTAINER_ID = R.id.summary_page_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_my_event, container, false);

        // init
        mChildFragmentUtils = new ChildFragmentUtils(this);

        // matching views
        matchingView( v );

        // display summary page
        summaryPage();

        return v;
    }

    /** Feature methods */
    private void summaryPage(){
        mChildFragmentUtils.replaceChildFragment(SUMMARY_PAGE_CONTAINER_ID, new SummaryPage());
    }

    /** Matching views */
    private void matchingView( View v ){

    }
}
