package com.think.runex.java.Utils.Location;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;

import java.util.concurrent.Executor;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class FusedLocationProviderUtils {
    /**
     * Main variables
     */
    private final String ct = "FusedLocationProviderUtils->";

    // instance variables
    private Activity mActivity;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private FusedLocationProviderUtils(Activity activity) {
        this.mActivity = activity;
        init();
    }

    public static FusedLocationProviderUtils newInstance(Activity activity) {
        return new FusedLocationProviderUtils(activity);

    }

    /** Implement methods */

    /** Feature methods */
    public void stop(){
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

    }
    public void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            // prepare usage variables
            final String mtn = ct + "getLastLocation() ";

            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude());

                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
                }
            }
        });


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5 * 1000);
        mLocationCallback = new LocationCallback() {
            // prepare usage variables
            final String mtn = ct + "LocationCallback() ";

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude());

                        L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
                    }
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

    }
}
