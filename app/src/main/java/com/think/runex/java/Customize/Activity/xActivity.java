package com.think.runex.java.Customize.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;

public class xActivity extends FragmentActivity implements xActivityHandler {
    /**
     * Main variables
     */
    private final String ct = "xActivity->";
    public FragmentUtils fragmentManager;
    public int containerId = -1;

    public void removeChildFragments() {
        // prepare usage variables
        final int fragmentAmount = getSupportFragmentManager().getFragments().size();

        for (int a = 0; a < getSupportFragmentManager().getFragments().size(); a++) {
            // prepare usage variables
            xFragment fragment = (xFragment) getSupportFragmentManager().getFragments().get(a);

            if(fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                fragment.getChildFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentManager = FragmentUtils.newInstance(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentCallback(xTalk xTalk) {
        final String mtn = ct + "onFragmentCallback() ";
        L.i(mtn + "Callback");

    }

    /**
     * Setter
     */
    public xActivity setFragmentContainerId(int containerId) {
        this.containerId = containerId;
        return this;
    }

}
