package com.think.runex.java.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    /**
     * Main variables
     */
    private final String ct = "PermissionUtils->";

    // instance variables
    private Activity mActivity;

    public static PermissionUtils newInstance(Activity activity) {
        return new PermissionUtils(activity);
    }

    private PermissionUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * Feature methods
     */
    public boolean checkPermission(String permissionName) {
        return ContextCompat.checkSelfPermission(mActivity, permissionName)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(int requestCode, String... permissions) {
        // No explanation needed; request the permission
        ActivityCompat.requestPermissions(mActivity,
                permissions,
                requestCode);


    }
}
