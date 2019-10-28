package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
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
    private List<LatLng> points = new ArrayList<>();
    private Activity mActivity;
    private GoogleMap mMap;
    private Polyline mLastPolyline;
    public double distance = 0.0;

    private GoogleMapUtils(Activity activity, GoogleMap map){
        this.mActivity = activity;
        this.mMap = map;
    }
    public static GoogleMapUtils newInstance(Activity activity, GoogleMap map){
        return new GoogleMapUtils( activity, map );
    }


    /** Feature methods */
    public void addOnce(){
        // prepare usage variables
        final LatLng[] latlngs = new LatLng[points.size()];
        for( int a = 0; a < points.size(); a++){
            latlngs[a] = new LatLng(points.get( a ).latitude, points.get( a ).longitude );

        }
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(Configs.GoogleMap.Polyline.WIDTH)
                .add( latlngs ));

        polyline.setColor(color);
    }
    public void addPolyline(xLocation from, xLocation to){
        // start point
        if( points.size() <= 0 ) points.add(new LatLng(from.latitude, from.longitude));

        // end point
        points.add( new LatLng(to.latitude, to.longitude) );

        // hide last polyline
        if( mLastPolyline != null ) mLastPolyline.setVisible(false);

        // prepare usage variables
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(Configs.GoogleMap.Polyline.WIDTH)
                .add( points.toArray( new LatLng[points.size()]) ));
//                .add(new LatLng(from.latitude, from.longitude),
//                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

        // keep last polyline
        mLastPolyline = polyline;

        // calculate kilometers
        distance += calculateTwoCoordinates(from, to);

    }

    private double calculateTwoCoordinates(xLocation from, xLocation to) {
        // prepare usage variables
        final String mtn = ct + "calculateTwoCoordinates() ";
        double lat1 = from.latitude;
        double lat2 = to.latitude;
        double lon1 = from.latitude;
        double lon2 = to.latitude;
        double radlat1 = Math.PI * lat1 / 180;
        double radlat2 = Math.PI * lat2 / 180;
        double theta = lon1 - lon2;
        double radtheta = Math.PI * theta / 180;
        double dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        if (dist > 1) {
            dist = 1;
        }
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        double kilometer = dist * 1.609344;

        return kilometer;
    }



}
