package com.think.runex.java.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class LocationUtils implements LocationListener {
    /**
     * Main variables
     */
    private final String ct = "LocationUtils->";

    // instance variables
    private Activity mActivity;
    private LocationManager mLocationManager;
    private PermissionUtils mPermissionUtils;

    public static LocationUtils newInstance(Activity activity) {
        return new LocationUtils(activity);
    }

    private LocationUtils(Activity activity) {
        mActivity = activity;
        mPermissionUtils = PermissionUtils.newInstance(activity);
        mLocationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);

    }


    /**
     * Implement methods
     */
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // prepare usage variables
        final String mtn = ct +"onStatusChanged() ";

        L.i(mtn +"");

    }

    @Override
    public void onLocationChanged(Location location) {
        // prepare usage variables
        final String mtn = ct +"onLocationChanged() ";

        L.i(mtn +"");

    }

    @Override
    public void onProviderEnabled(String s) {
        // prepare usage variables
        final String mtn = ct +"onProviderEnabled() ";

        L.i(mtn +"");

    }

    @Override
    public void onProviderDisabled(String s) {
        // prepare usage variables
        final String mtn = ct +"onProviderDisabled() ";

        L.i(mtn +"");

    }

    /**
     * Feature methods
     */
    private void requestLocationUpdate() {
        // prepare usage variables
        final String mtn = ct +"requestLocationUpdate() ";

        try {
            if (mPermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000L,
                        1.0f,
                        this);

            }

        } catch (Exception e) {
            L.e(mtn +"Err: "+ e);

        }
    }

    public boolean isGPSAvailable() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mActivity.startActivityForResult(i, 0);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity

                }
            });
            builder.create().show();
        }

        return false;
    }

}
