package com.think.runex.java.Utils;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.think.runex.java.Constants.Constants;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Pages.MainPage;

public class FragmentUtils {

    /**
     * Main variables
     */
    private final String ct = "FragmentUitls->";
    public int containerId = -1;
    public FragmentActivity activity = null;

    private FragmentUtils(FragmentActivity activity, int containerId) {
        this.containerId = containerId;
        this.activity = activity;
    }

    private FragmentUtils(FragmentActivity activity) {
        this.activity = activity;
    }

    public static FragmentUtils newInstance(FragmentActivity activity, int containerId) {
        return new FragmentUtils(activity, containerId);
    }

    public static FragmentUtils newInstance(FragmentActivity activity) {
        return new FragmentUtils(activity);
    }

    public int getStackCount() {
        return activity.getSupportFragmentManager().getBackStackEntryCount();
    }

    public void removeAllFragment() {
        while (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void replaceFragmentWithAnim(Fragment fragment) {
        // prepare usage variables
        final FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.setCustomAnimations(android.R.anim.slide_in_left, 0, 0,
                android.R.anim.slide_out_right);
        t.addToBackStack(Constants.Fragment.TAG());
        t.replace(containerId, fragment);
        t.commit();
    }

    public void replaceFragment(Fragment fragment) {
        // prepare usage variables
        final FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.addToBackStack(Constants.Fragment.TAG());
        t.replace(containerId, fragment);
        t.commit();
    }


    public void removeFragment(Fragment fragment) {
        // prepare usage variables
        final String mtn = ct + "removeFragment() ";
        final FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();

        t.remove(fragment).commit();

    }

    public void addFragment(int containerId, Fragment fragment) {
        addFragment(containerId, fragment, false);
    }
    public void addFragment(int containerId, Fragment fragment, boolean withBackStack) {
        // prepare usage variables
        final String mtn = ct + "addFragment() ";
        final FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) {
            L.i(mtn + "show");
            t.show(fragment);

        } else {
            L.i(mtn + "added");
            t.add(containerId, fragment);

        }

        if (withBackStack) t.addToBackStack(Constants.Fragment.TAG());
        t.commit();
    }

}
