package com.think.runex.java.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtils {
    /**
     * Main variables
     */
    private final String ct = "PermissionUtils->";

    // instance variables
    private Activity mActivity;
    private Fragment mFragment;

    public static PermissionUtils newInstance(Activity activity) {
        return new PermissionUtils(activity);
    }

    public static PermissionUtils newInstance(Fragment fragment) {
        return new PermissionUtils(fragment);
    }

    private PermissionUtils(Activity activity) {
        mActivity = activity;
    }

    private PermissionUtils(Fragment fragment) {
        mFragment = fragment;
    }

    /**
     * Feature methods
     */
    public boolean checkPermission(String permissionName) {
        if (mFragment != null) {
            return ContextCompat.checkSelfPermission(mFragment.requireContext(), permissionName)
                    == PackageManager.PERMISSION_GRANTED;
        } else if (mActivity != null) {
            return ContextCompat.checkSelfPermission(mActivity, permissionName)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }

    public void requestPermissions(int requestCode, String... permissions) {
        if (mFragment != null) {
            mFragment.requestPermissions(permissions, requestCode);

            // No explanation needed; request the permission
        } else if (mActivity != null) {
            ActivityCompat.requestPermissions(mActivity,
                    permissions,
                    requestCode);
        }
    }
}
