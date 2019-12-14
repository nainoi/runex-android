package com.think.runex.java.Utils.Location;

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

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.PermissionUtils;

public class LocationUtils implements LocationListener {
    /**
     * Main variables
     */
    private final String ct = "LocationUtils->";

    // instance variables
    private Activity mActivity;
    private LocationManager mLocationManager;
    private PermissionUtils mPermissionUtils;
    private LocationTrackingCallback mListener;

    public static LocationUtils newInstance(Activity activity) {
        return new LocationUtils(activity);
    }

    private LocationUtils(Activity activity) {
        mActivity = activity;
        mPermissionUtils = PermissionUtils.newInstance(activity);

        init();

    }


    /**
     * Implement methods
     */
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // prepare usage variables
        final String mtn = ct +"onStatusChanged() ";

//        L.i(mtn +"");

    }

    @Override
    public void onLocationChanged(Location location) {
        // prepare usage variables
        final String mtn = ct +"onLocationChanged() ";

        if( mListener != null ){
            mListener.onLocationChanged( location );

        } else L.e(mtn +"mListener["+ mListener +"] is not ready.");

//        L.i(mtn +"location: "+ location.getLatitude() +" : "+ location.getLongitude());

    }

    @Override
    public void onProviderEnabled(String s) {
        // prepare usage variables
        final String mtn = ct +"onProviderEnabled() ";

//        L.i(mtn +"");

    }

    /**
     * Feature methods
     */
    public Location getLastKnownLocation(){
        if( mPermissionUtils.checkPermission(Globals.ACCESS_FINE_LOCAITON)) {
            return mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        return null;
    }
    public void addLocationListener( LocationTrackingCallback listener){
        this.mListener = listener;
    }

    @Override
    public void onProviderDisabled(String s) {
        // prepare usage variables
        final String mtn = ct +"onProviderDisabled() ";

        L.i(mtn +"");

    }
    private void init(){
        mLocationManager = (LocationManager) mActivity
                .getSystemService(Context.LOCATION_SERVICE);
    }
    public void stop(){
        mLocationManager.removeUpdates( this );
        mLocationManager = null;

    }
    public void start() {
        // prepare usage variables
        final String mtn = ct +"requestLocationUpdate() ";

        // init new location manager
        if( mLocationManager == null ) init();

        try {
            if (mPermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        Configs.LocationTracker.MIN_TIME,
//                        Configs.LocationTracker.MIN_DISTANCE,
//                        this);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        Configs.LocationTracker.MIN_TIME,
                        Configs.LocationTracker.MIN_DISTANCE,
                        this);

            }

        } catch (Exception e) {
            L.e(mtn +"Err: "+ e);

        }
    }

    public boolean isGPSAvailable() {
        if( mLocationManager == null ) init();

        // prepare usage variables
        final String mtn = ct +"isGPSAvailable() ";
        final boolean gpsProvider = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean gpsNetworkProvider = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        L.i(mtn +"gpsProvider: "+ gpsProvider);
        L.i(mtn +"gpsNetworkProvider: "+ gpsNetworkProvider);
        if (!gpsProvider && !gpsNetworkProvider ) {
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

            return false;

        }

        return true;

    }

}
