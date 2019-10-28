package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.think.runex.java.App.Configs;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtils {
    /** Main variables */
    private final String ct = "GoogleMapUtils->";

    // instance variables
    private List<xLocation> points = new ArrayList<>();
    private Activity mActivity;
    private GoogleMap mMap;

    private GoogleMapUtils(Activity activity, GoogleMap map){
        this.mActivity = activity;
        this.mMap = map;
    }
    public static GoogleMapUtils newInstance(Activity activity, GoogleMap map){
        return new GoogleMapUtils( activity, map );
    }


    /** Feature methods */
    public void addPolyline(xLocation from, xLocation to){
        // start point
        if( points.size() <= 0 ) points.add(from);

        // end point
        points.add( to );

        // prepare usage variables
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(Configs.GoogleMap.Polyline.WIDTH)
                .add(new LatLng(from.latitude, from.longitude),
                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

    }


}
