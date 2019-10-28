package com.think.runex.java.Customize;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.think.runex.java.Pages.Summary.onTabChangedListener;

public class xFragment extends Fragment {
    public String title = "-";
    public int position = 0;
    public onTabChangedListener tabChangedListener;
    public FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = getActivity();
        super.onCreate(savedInstanceState);
    }

    public xFragment setOnTabChangedListener(onTabChangedListener listener ){
        this.tabChangedListener = listener;
        return this;
    }
    public xFragment setPosition(int position ){
        this.position = position;
        return this;
    }
    public xFragment setTitle( String title){
        this.title = title;
        return this;
    }
}
