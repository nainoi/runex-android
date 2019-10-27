package com.think.runex.java.Customize;

import androidx.fragment.app.Fragment;

import com.think.runex.java.Pages.Summary.onTabChangedListener;

public class xFragment extends Fragment {
    public String title = "-";
    public int position = 0;
    public onTabChangedListener tabChangedListener;

    public xFragment setOnTabChangedListener( onTabChangedListener listener ){
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
