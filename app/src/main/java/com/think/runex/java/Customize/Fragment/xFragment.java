package com.think.runex.java.Customize.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Pages.Summary.onTabChangedListener;

public class xFragment extends Fragment implements xFragmentHandler {
    public String title = "-";
    public int position = 0;
    public onTabChangedListener tabChangedListener;
    public xActivity activity;
    public xTalk xTalk = new xTalk();

    @Override
    public xFragment setRequestCode(xTalk xTalk) {
        this.xTalk = xTalk;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = (xActivity)getActivity();
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
