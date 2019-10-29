package com.think.runex.java.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.xDump;
import com.think.runex.java.Services.BackgroundService;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.GoogleMap.GoogleMapUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.FusedLocationProviderUtils;
import com.think.runex.java.Utils.Location.LocationTrackingCallback;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.Recorder.RecorderUtils;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;

import java.text.DecimalFormat;

public class RecordActivity extends FragmentActivity implements OnMapReadyCallback
        , View.OnClickListener
//        , GoogleApiClient.ConnectionCallbacks
//        , GoogleApiClient.OnConnectionFailedListener
        , onRecorderCallback {
    /**
     * Main variables
     */
    private final String ct = "RecordActivity->";

    // instance variables
    private DecimalFormat df = new DecimalFormat("#.##");
    private LocationUtils mLocUtils;
    private PermissionUtils mPmUtils;
    private GoogleMapUtils mMapUtils;
    private GoogleMap mMap;
    private xLocation mLastLocation;
    private RecorderUtils mRecorderUtils;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // prepare usage variables
        final String mtn = ct + "BroadcastReceiver() ";

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // prepare usage variables
                final String jsonString = intent.getStringExtra(Globals.BROADCAST_LOCATION_VAL);
                final xLocation location = Globals.GSON.fromJson(jsonString, xLocation.class);

                if (mLastLocation == null) {
                    // keep last location
                    mLastLocation = new xLocation(location.latitude, location.longitude);

                    // exit from this process
                    return;

                }
                if (mMap == null) {
                    // log
                    L.i(mtn + "mMap[" + mMap + "] is not ready.");

                    // exit from this process
                    return;
                }

                L.i(mtn + "Location: " + location.latitude + ", " + location.longitude);

                // prepare usage variables
                LatLng ll = new LatLng(location.latitude, location.longitude);
                xLocation xFrom = mLastLocation;
                xLocation xTo = new xLocation(location.latitude, location.longitude);

                // test add polyline
                mMapUtils.addPolyline(xFrom, xTo);

                // update view
                lbDistance.setText(df.format(mMapUtils.distance) + " KM");

                // update props
                mLastLocation = xTo;

                if (!mZoomOnce) {
                    // update flag
                    mZoomOnce = true;

                    // move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
                }

            } catch (Exception e) {
                L.e(mtn + "Err: " + e);
            }
        }
    };

    // explicit variables
    private boolean mZoomOnce = false;

    // views
    private TextView lbTime;
    private TextView lbDistance;
    private Button btnStart, btnPause;


    /**
     * Implement methods
     */
    @Override
    public void onRecordTimeChanged(String time) {
        lbTime.setText(time);
    }

    @Override
    public void onClick(View view) {
        final String mtn = ct + "onClick() ";

        switch (view.getId()) {
            case R.id.btn_start:
                // start recording
                mRecorderUtils.start();

                // start service
                startService();

                break;
            case R.id.btn_pause:

                // stop service
                stopService();

                // pause
                mRecorderUtils.pause();

                // print
                mMapUtils.print10Location();

                break;
        }
    }

    /**
     * Service methods
     */
    public void startService() {
        // use this to start and trigger a service
        Intent i = new Intent(this, BackgroundService.class);
        // potentially add data to the intent
        i.putExtra("KEY1", "Value to be used by the service");

        startService(i);
    }

    // Stop the service
    public void stopService() {
        stopService(new Intent(this, BackgroundService.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // prepare usage variables
        final String mtn = ct + "onMapRead() ";

        // update props
        mMap = googleMap;

        //--> Map utils
        mMapUtils = GoogleMapUtils.newInstance(this, mMap);
        //--> Recorder utils
        mRecorderUtils = RecorderUtils.newInstance();
        mRecorderUtils.setRecorderCallback(this);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);

        mMap.setMyLocationEnabled(true);

        // prepare usage variables
        Location lkLoc = mLocUtils.getLastKnownLocation();

        // last known not found
        if (lkLoc != null) {
            // update flag
            mZoomOnce = true;

            // prepare usage variables
            LatLng ll = new LatLng(lkLoc.getLatitude(), lkLoc.getLongitude());

            // update map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));

        }

    }

//    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // prepare usage variables
        final String mtn = ct + "onCreate() ";

        // matching view
        matchingViews();

        // view event listener
        viewEventListener();

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
                if (mLastLocation == null) {
                    // keep last location
                    mLastLocation = new xLocation(location.getLatitude(), location.getLongitude());

                    // exit from this process
                    return;

                }
                if (mMap == null) {
                    // log
                    L.i(mtn + "mMap[" + mMap + "] is not ready.");

                    // exit from this process
                    return;
                }

                L.i(mtn + "Latitude: " + location.getLatitude() + ", " + location.getLongitude());

                // prepare usage variables
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                xLocation xFrom = mLastLocation;
                xLocation xTo = new xLocation(location.getLatitude(), location.getLongitude());

                // test add polyline
                mMapUtils.addPolyline(xFrom, xTo);

                // update props
                mLastLocation = xTo;

                if (!mZoomOnce) {
                    // update flag
                    mZoomOnce = true;

                    // move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
                }
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

                // start location
//                mLocUtils.start();

            }

        } else mPmUtils.requestPermissions(Globals.RC_PERMISSION, Globals.ACCESS_FINE_LOCAITON);


        if (!isGpsAvailable) L.i(mtn + "isGPSAvailable(" + isGpsAvailable + ") ");
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
    }

    /**
     * Matching views
     */
    private void matchingViews() {
        lbDistance = findViewById(R.id.lb_distance);
        lbTime = findViewById(R.id.lb_time);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
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
    protected void onDestroy() {
        super.onDestroy();

        // stop background service
        stopService();

        // unregister
        unregisterReceiver(broadcastReceiver);

        // prepare usage variables
        final String mtn = ct + "onDestroy() ";

        // log
        L.i(mtn);

    }

    @Override
    protected void onStop() {
        // prepare usage variables
        final String mtn = ct + "onStop() ";

        // log
        L.i(mtn);
//        mLocUtils.stop();

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct + "onResume() ";

        // register broadcast
        registerReceiver(broadcastReceiver, new IntentFilter("BROADCAST"));

        // log
        L.i(mtn);

        // conditions
        beforeGPSRecording();
    }
}
