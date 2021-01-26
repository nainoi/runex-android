package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;

import com.google.android.libraries.maps.CameraUpdate;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.Polyline;
import com.google.android.libraries.maps.model.PolylineOptions;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtils {
    /**
     * Main variables
     */
    private final String ct = "GoogleMapUtils->";

    // instance variables
    public List<LatLng> points = new ArrayList<>();

    private GoogleMap mMap;
    private Polyline mLastPolyline;
    public double distance = 0.0;

    // explicit variables
    private final float MAP_TRACKING_ZOOM_LEVEL = 17f;

    private GoogleMapUtils( GoogleMap map) {
        this.mMap = map;
    }

    public static GoogleMapUtils newInstance(GoogleMap map) {
        return new GoogleMapUtils( map);
    }

    /**
     * Feature methods
     */
    public void tracking(xLocation xLoc){
        // to current location
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(xLoc.latitude, xLoc.longitude), MAP_TRACKING_ZOOM_LEVEL));

    }
    public void zoomToFit() {
        // exit from this process
        // when points are not ready
        // to do the fit
        if (points.size() <= 1) return;

        // prepare usage variables
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng ll : points) {
            // scope
            builder.include(ll);
        }
        //--> ll bounds
        LatLngBounds bounds = builder.build();

        // camera update
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds( bounds, 65 );

        // move camera runex@password
        mMap.animateCamera(cameraUpdate);

        // zoom fit
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // zoom out anim
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), mMap.getCameraPosition().zoom - 1));

            }
        }, 900);


    }

    public void print10Location() {
        final String mtn = ct + "print10Location() ";
        List<LatLng> temps = new ArrayList<>();

        for (int a = 0; a < points.size(); a++) {
            temps.add(points.get(a));

            if (temps.size() == 10) {
                L.i(mtn + "LL[" + a + "]" + Globals.GSON.toJson(temps));
                temps.clear();
            }
        }

        L.i(mtn + "LL[Last]" + Globals.GSON.toJson(temps));


    }

    public void clearAll() {
        clearDistance();
        clearPolyline();
    }

    public void clearDistance() {
        distance = 0.0;
    }

    public void clearPolyline() {
        if (mLastPolyline != null) mLastPolyline.remove();

        // clear point storage
        points.clear();

    }

    public void addDistance(xLocation from, xLocation to) {
        final double diffDistance = difDistance(from, to);
        if( diffDistance > 0.005 ) { distance += diffDistance; }
    }

    public double difDistance(xLocation from, xLocation to) {
        // calculate kilometers
        return calculateTwoCoordinates(from, to);
    }

    public void redrawPolyline() {
        // prepare usage variables
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(10)
                .add(points.toArray(new LatLng[points.size()])));
//                .add(new LatLng(from.latitude, from.longitude),
//                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

        // gone last polyline
        if (mLastPolyline != null) mLastPolyline.remove();

        // keep last polyline
        mLastPolyline = polyline;
    }
    public void addPolyline(xLocation from, xLocation to) {
        // start point
        if (points.size() <= 0) points.add(new LatLng(from.latitude, from.longitude));

        // end point
        points.add(new LatLng(to.latitude, to.longitude));

        // prepare usage variables
//        final float polyWidth = mMap.getCameraPosition().zoom % 3 == 0
//                ? mMap.getCameraPosition().zoom / 3
//                : 5;
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(10)
                .add(points.toArray(new LatLng[points.size()])));
//                .add(new LatLng(from.latitude, from.longitude),
//                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

        // gone last polyline
        if (mLastPolyline != null) mLastPolyline.remove();

        // keep last polyline
        mLastPolyline = polyline;

    }

    public double calculateTwoCoordinates(xLocation from, xLocation to) {
        // prepare usage variables
        final String mtn = ct + "calculateTwoCoordinates() ";
        double lat1 = from.latitude;
        double lat2 = to.latitude;
        double lng1 = from.longitude;
        double lng2 = to.longitude;
        double distance = (((Math.acos(Math.sin(lat1 * Math.PI / 180)
                * Math.sin(lat2 * Math.PI / 180) + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180)
                * Math.cos((lng1 - lng2) * Math.PI / 180))
                * 180 / Math.PI) * 60 * 1.1515) * 1.609344);

        // is NaN
        if (Double.isNaN(distance)) return 0;

        // return
        return distance;
    }
}
