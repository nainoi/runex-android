package com.think.runex.java.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Pages.SendRunningResultPage;
import com.think.runex.java.Services.BackgroundService;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.Animation.AnimUtils;
import com.think.runex.java.Utils.Animation.onAnimCallback;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.GoogleMap.GoogleMapUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.LocationTrackingCallback;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.Recorder.RecorderUtils;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;

import java.io.File;
import java.text.DecimalFormat;

import cl.jesualex.stooltip.Position;
import cl.jesualex.stooltip.Tooltip;

public class RecordActivity extends FragmentActivity implements OnMapReadyCallback
        , View.OnClickListener
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
                mMapUtils.addDistance(xFrom, xTo);
                mRecorderUtils.addDistance(mMapUtils.difDistance(xFrom, xTo));

                // update view
                binding();

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
    private boolean mOnRecording = false;
    private boolean mInitMap = false;
    private final int CONTAINER_ID = R.id.display_fragment_frame;

    // views
    private TextView lbRecordingState;
    private ImageView icRecordState;
    private TextView lbTime;
    private TextView lbDistance;
    private TextView btnStart;
    private View btnStopAndSubmit;

    /**
     * Implement methods
     */
    @Override
    public void onRecordTimeChanged(String time) {
        binding();

    }

    @Override
    public void onClick(View view) {
        final String mtn = ct + "onClick() ";

        switch (view.getId()) {
            case R.id.btn_start:

                // going to stop recording
                if (mOnRecording) {
                    // log
                    L.i(mtn + "on recording. -> going to pause recording.");

                    // paused
                    paused();

                    // exit from this process
                    return;
                }

                // start
                start();

                break;

            case R.id.frame_stop_and_submit:

                // paused
                paused();

                // prepare recording object
                RecorderObject recorderObj = new RecorderObject();
                recorderObj.setDistanceKm( mRecorderUtils.mRecordDistanceKm );
                recorderObj.setRecordingTime( mRecorderUtils.mRecordTime );

                // reset result
                reset();

                // refresh views
                binding();

                toSendRunningResultPage( recorderObj );

                // hide stop button
                btnStopAndSubmit.setVisibility(View.GONE);

                // change start button
                // description
                lbRecordingState.setText(getString(R.string.start_recording));

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
        actUtls.fullScreen();

        //--> Location utils
        mLocUtils = LocationUtils.newInstance(this);
        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(this);

        btnStopAndSubmit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // hide
                btnStopAndSubmit.setVisibility(View.GONE);

                // remove listener
                btnStopAndSubmit.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

    }

    /**
     * Feature methods
     */
    private void binding(){
        lbTime.setText(mRecorderUtils.mRecordDisplayTime);
        lbDistance.setText(df.format(mRecorderUtils.mRecordDistanceKm) + "km");

    }
    private void animScaleOut() {
        AnimUtils.instance()
                .scaleOut(btnStopAndSubmit, new onAnimCallback() {
                    @Override
                    public void onEnd() {

                    }

                    @Override
                    public void onStart() {
                        btnStopAndSubmit.setVisibility(View.VISIBLE);

                    }
                });

    }

    private void anim(int drawerId) {
        AnimUtils.instance().fadeOut(icRecordState, 100, 0, new onAnimCallback() {
            @Override
            public void onEnd() {
                AnimUtils.instance().fadeIn(icRecordState, 150, 0, new onAnimCallback() {
                    @Override
                    public void onEnd() {
                    }

                    @Override
                    public void onStart() {
                        icRecordState.setImageDrawable(getDrawable(drawerId));

                    }
                });

            }

            @Override
            public void onStart() {
            }
        });
    }

    private void start(){
        // update flag
        mOnRecording = true;

        // start recording
        startRecording();

        // anim
        anim(android.R.drawable.ic_media_pause);

        // recording state description
        lbRecordingState.setText(getString(R.string.pause_recording));
    }
    private void reset(){
        mRecorderUtils.reset();
        mMapUtils.clearAll();
    }
    private void paused(){
        // anim pause
        anim(android.R.drawable.ic_media_play);

        // pause recording
        pauseRecording();

        // clear flag
        mOnRecording = false;

        // scale out animation
        // condition
        if( btnStopAndSubmit.getVisibility() != View.VISIBLE ) {
            // scale stop button
            animScaleOut();

        }

        // recording state description
        lbRecordingState.setText(getString(R.string.resume_recording));
    }

    private void startRecording() {
        // start recording
        mRecorderUtils.start();

        // start service
        startService();
    }

    private void pauseRecording() {
        // stop service
        stopService();

        // pause
        mRecorderUtils.pause();
    }

    private void toSendRunningResultPage(RecorderObject recorder) {
        // prepare usage variables
        FragmentUtils fu = FragmentUtils.newInstance(this, CONTAINER_ID);
        fu.replaceFragmentWithAnim(new SendRunningResultPage().setRecorder(recorder));
    }

    private void beforeGPSRecording() {
        // prepare usage variables
        final String mtn = ct + "beforeGPSRecording() ";
        boolean isGpsAvailable = false;

        if (mPmUtils.checkPermission(Globals.ACCESS_FINE_LOCAITON)) {
            if (isGpsAvailable = mLocUtils.isGPSAvailable()) {
                L.i(mtn + "Ready to record GPS.");

                // condition
                if( mInitMap ) return;

                // update flag
                mInitMap = true;

                // init map
                initMap();

            }

        } else mPmUtils.requestPermissions(Globals.RC_PERMISSION, Globals.ACCESS_FINE_LOCAITON);


        if (!isGpsAvailable) L.i(mtn + "isGPSAvailable(" + isGpsAvailable + ") ");
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnStart.setOnClickListener(this);
        btnStopAndSubmit.setOnClickListener(this);
    }

    /**
     * Matching views
     */
    private void matchingViews() {
        lbDistance = findViewById(R.id.lb_distance);
        lbTime = findViewById(R.id.lb_time);
        lbRecordingState = findViewById(R.id.lb_recording_state_description);
        btnStart = findViewById(R.id.btn_start);
        btnStopAndSubmit = findViewById(R.id.frame_stop_and_submit);
        icRecordState = findViewById(R.id.ic_recording_state);

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
