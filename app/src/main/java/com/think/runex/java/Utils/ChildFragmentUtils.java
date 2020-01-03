package com.think.runex.java.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.think.runex.java.Constants.Constants;

import java.util.List;

public class ChildFragmentUtils extends Fragment {
    /**
     * Main variables
     */
    private final String ct = "ChildFragmentUtils->";

    // instance variables
    private FragmentManager childFragmentManager;

    public static ChildFragmentUtils newInstance(Fragment parentFragment) {
        return new ChildFragmentUtils(parentFragment);
    }

    private ChildFragmentUtils(Fragment parentFragment) {
        this.childFragmentManager = parentFragment.getChildFragmentManager();

    }

    private void showAllChild(Fragment parent) {
        // prepare usage variables
        List<Fragment> nestedChild = parent.getChildFragmentManager().getFragments();

        if (nestedChild.size() > 0) {
            // prepare usage variables
            final FragmentTransaction trans = parent.getChildFragmentManager().beginTransaction();

            // show all nested fragment
            for (Fragment fm : nestedChild) {
                trans.show( fm );

            }

            // commit
            trans.commit();
        }
    }

    public void addChildFragment(int childContainerId, Fragment fragment) {
        addChildFragment(childContainerId, fragment, false);
    }

    public void addChildFragment(int childContainerId, Fragment fragment, boolean withBackStack) {
        // prepare usage variables
        final String mtn = ct + "addChildFragment(" + fragment.getClass().getSimpleName() + ") ";
        final FragmentTransaction t = childFragmentManager.beginTransaction();

        if (fragment.isAdded()) {
            L.i(mtn + "show");
            t.show(fragment);

            // show nested child fragment
//            showAllChild( fragment );

        } else {
            L.i(mtn + "added");
            t.add(childContainerId, fragment);

        }

//        if(withBackStack) t.addToBackStack(Constants.Fragment.TAG());
        t.commitAllowingStateLoss();

    }

    public void printInfo(Fragment fragment){
        // prepare usage variables
        final String mtn = ct +"printInfo("+ fragment.getClass().getSimpleName() +") ";
//        L.i(mtn);
//        L.i(mtn +" * * * Fragment Information * * *");
//        L.i(mtn +"isAdded: "+ fragment.isAdded());
//        L.i(mtn +"isVisible: "+ fragment.isVisible());
//        L.i(mtn +"isHidden: "+ fragment.isHidden());
//        L.i(mtn +"isMenuVisible: "+ fragment.isMenuVisible());
//        L.i(mtn +"isInLayout: "+ fragment.isInLayout());
//        L.i(mtn);
//        L.i(mtn);

    }
}
