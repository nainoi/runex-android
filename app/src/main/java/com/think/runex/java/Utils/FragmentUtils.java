package com.think.runex.java.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.think.runex.java.Constants.Constants;
import com.think.runex.java.Pages.MainPage;

public class FragmentUtils {

    /**
     * Main variables
     */
    public static int containerId = -1;
    public static FragmentActivity activity = null;

    public static int getStackCount(){
        return activity.getSupportFragmentManager().getBackStackEntryCount();
    }

    public static void replaceFragment(Fragment fragment) {
        // prepare usage variables
        final FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.addToBackStack(Constants.Fragment.TAG());
        t.replace(containerId, fragment);
        t.commit();
    }
}
