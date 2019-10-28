package com.think.runex.java.Activities;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.think.runex.R;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.LocationUtils;
import com.think.runex.java.Utils.PermissionUtils;

public class RecordActivity extends FragmentActivity implements OnMapReadyCallback {
    /** Main variables */
    private final String ct = "RecordActivity->";

    // instance variables
    private LocationUtils mLocUtils;
    private PermissionUtils mPmUtils;
    private GoogleMap mMap;

    /** Implement methods */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // prepare usage variables
        final String mtn = ct +"onMapRead() ";

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_record);

        // prepare usage variables
        final String mtn = ct +"onCreate() ";

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance( this );
        actUtls.activity = this;
        actUtls.fullScreen();
        //--> Location utils
        mLocUtils = LocationUtils.newInstance( this );

        // gps provider
        boolean isGpsAvailable = false;
        if( ( isGpsAvailable = mLocUtils.isGPSAvailable()) ){


        } else

        L.i(mtn +"isGPSAvailable("+ isGpsAvailable +") ");

        // init map
        initMap();

    }

    /** Initial GoogleMap */
    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


}
