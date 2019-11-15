package com.think.runex.java.Customize.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;
import com.think.runex.java.Constants.Priority;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Pages.Summary.onTabChangedListener;
import com.think.runex.java.Utils.L;

import java.util.List;

public class xFragment extends Fragment implements xFragmentRequestCode
        , xFragmentHandler {
    /**
     * Main variables
     */
    private final String ct = "xFragment->";

    public String title = "-";
    public int position = 0;
    public xActivity activity;
    public xTalk xTalk = new xTalk();
    public int childContainerId = -1;
    public Priority PRIORITY = Priority.NONE;

    // instance variables
    public onTabChangedListener tabChangedListener;
    public xFragmentHandler fragmentHandler;

    public boolean onBackPressed(xFragment fragment) {
        // prepare usage variables
        final String mtn = ct + "onBackPressed() ";

        // conditions
        if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
//            L.i(mtn + "on back-stack entry count > 0");
            // pop back stack
            fragment.getChildFragmentManager().popBackStackImmediate();

            // exit from this process
            return true;

        } else if (getCurrentDisplayedFragment("onBackPressed-Condition", fragment) != null) {
//            L.i(mtn + "on get current displayed fragment != null");

            // prepare usage variables
            Fragment displayedFragment = getCurrentDisplayedFragment("onBackPressed-Condition-Accepted", fragment);

            // remove displayed fragment
            displayedFragment.getParentFragment()
                    .getChildFragmentManager()
                    .beginTransaction()
                    .remove(displayedFragment)
                    .commitAllowingStateLoss();

            // exit from this process
            return true;

        } else {
            if (fragment.PRIORITY.ID == Priority.PARENT.ID) return false;

            L.i(mtn + "remove fragment by own parent.");
            // remove by fragment parent
            fragment.getParentFragment().getChildFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();

            // exit from this process
            return true;

        }

    }

    public xFragment getCurrentDisplayedFragment(String tag, Fragment parentFragment) {
        // prepare usage variables
        final String mtn = ct + "getCurrentDisplayedFragment() ";
        List<Fragment> fragments = parentFragment.getChildFragmentManager().getFragments();
        Fragment visibleFragment = null;

        if (fragments.size() <= 0) {
            L.i(mtn + "- - - Closed  - - -");

            // exit from this process
            return (xFragment) visibleFragment;
        }

        L.i(mtn + "* * * Fragment(" + tag + ") * * *");
        for (Fragment fragment : fragments) {
            L.i(mtn + "Name: " + fragment.getClass().getSimpleName());
            L.i(mtn + "isVisible: " + fragment.isVisible());
            L.i(mtn + "isHidden: " + fragment.isHidden());
            L.i(mtn + "- - - - - - - - - -");

//             visible fragment
            if (fragment.isVisible()) {
                // prepare usage variables
                Fragment temp = getCurrentDisplayedFragment("Recursive", fragment);

                if (temp != null) {
                    visibleFragment = temp;

                } else visibleFragment = fragment;

                break;
            }
        }

        return (visibleFragment == null || !(visibleFragment instanceof xFragment)) ? null : (xFragment) visibleFragment;
    }

    public int getChildCount() {
        return getChildFragmentManager().getBackStackEntryCount();
    }

    @Override
    public xFragment setRequestCode(xTalk xTalk) {
        this.xTalk = xTalk;
        return this;
    }

    @Override
    public xFragment onResult(xTalk xTalk) {
        if( fragmentHandler != null ) {
            fragmentHandler.onResult(xTalk);

        }

        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = (xActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    public xFragment setOnTabChangedListener(onTabChangedListener listener) {
        this.tabChangedListener = listener;
        return this;
    }

    public xFragment setPriority(Priority priority) {
        this.PRIORITY = priority;
        return this;
    }

    public xFragment setPosition(int position) {
        this.position = position;
        return this;
    }

    public xFragment setFragmentHandler(xFragmentHandler handler) {
        this.fragmentHandler = handler;
        return this;
    }

    public xFragment setChildContainerId(int childContainerId) {
        this.childContainerId = childContainerId;
        return this;
    }

    public xFragment setTitle(String title) {
        this.title = title;
        return this;
    }

}
