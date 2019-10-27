package com.think.runex.java.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.think.runex.java.Constants.Constants;

public class ChildFragmentUtils extends Fragment {
    /** Main variables */
    private final String ct = "ChildFragmentUtils->";

    // instance variables
    private FragmentManager childFragmentManager;

    public ChildFragmentUtils(Fragment parentFragment){
        this.childFragmentManager = parentFragment.getChildFragmentManager();

    }

    public void replaceChildFragment(int childContainerId, Fragment fragment) {
        // prepare usage variables
        final FragmentTransaction t = childFragmentManager.beginTransaction();
        t.addToBackStack(Constants.Fragment.TAG());
        t.replace(childContainerId, fragment);
        t.commit();

    }
}
