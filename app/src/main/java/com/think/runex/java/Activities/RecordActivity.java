package com.think.runex.java.Activities;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.LocationTrackingCallback;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.PermissionUtils;

public class RecordActivity extends FragmentActivity implements OnMapReadyCallback {
    /**
     * Main variables
     */
    private final String ct = "RecordActivity->";

    // instance variables
    private LocationUtils mLocUtils;
    private PermissionUtils mPmUtils;
    private GoogleMap mMap;

    // explicit variables

    /**
     * Implement methods
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // prepare usage variables
        final String mtn = ct + "onMapRead() ";

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);

        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // prepare usage variables
        final String mtn = ct + "onCreate() ";

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance(this);
        actUtls.activity = this;
        actUtls.fullScreen();
        //--> Location utils
        mLocUtils = LocationUtils.newInstance(this);
        mLocUtils.addLocationListener(new LocationTrackingCallback() {
            // prepare usage variables
            final String mtn = ct + "addLocationListener() ";

            @Override
            public void onLocationChanged(Location location) {
                if (mMap == null) {
                    // log
                    L.i(mtn + "mMap[" + mMap + "] is not ready.");

                    // exit from this process
                    return;
                }

                L.i(mtn +"Latitude: "+ location.getLatitude() +", "+ location.getLongitude());

                // prepare usage variables
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                // move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
            }
        });
        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(this);

        // init map
        initMap();

    }

    /**
     * Feature methods
     */
    private void beforeGPSRecording() {
        // prepare usage variables
        final String mtn = ct + "beforeGPSRecording() ";
        boolean isGpsAvailable = false;

        if (mPmUtils.checkPermission(Globals.ACCESS_FINE_LOCAITON)) {
            if (isGpsAvailable = mLocUtils.isGPSAvailable()) {
                L.i(mtn + "Ready to record GPS.");

                //
                mLocUtils.start();

            }

        } else mPmUtils.requestPermissions(Globals.RC_PERMISSION, Globals.ACCESS_FINE_LOCAITON);


        if (!isGpsAvailable) L.i(mtn + "isGPSAvailable(" + isGpsAvailable + ") ");
    }

    /**
     * Initial GoogleMap
     */
    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Life cycle
     */
    @Override
    protected void onStop() {
        mLocUtils.stop();

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // conditions
        beforeGPSRecording();
    }
}
