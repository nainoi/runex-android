package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
    private List<LatLng> points = new ArrayList<>();
    private Activity mActivity;
    private GoogleMap mMap;
    private Polyline mLastPolyline;
    public double distance = 0.0;

    private GoogleMapUtils(Activity activity, GoogleMap map) {
        this.mActivity = activity;
        this.mMap = map;
    }

    public static GoogleMapUtils newInstance(Activity activity, GoogleMap map) {
        return new GoogleMapUtils(activity, map);
    }

    /**
     * Feature methods
     */
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

    public void addOnce() {
        // prepare usage variables
        final LatLng[] latlngs = new LatLng[points.size()];
        for (int a = 0; a < points.size(); a++) {
            latlngs[a] = new LatLng(points.get(a).latitude, points.get(a).longitude);

        }
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(Configs.GoogleMap.Polyline.WIDTH)
                .add(latlngs));

        polyline.setColor(color);
    }

    public void addPolyline(xLocation from, xLocation to) {
        // start point
        if (points.size() <= 0) points.add(new LatLng(from.latitude, from.longitude));

        // end point
        points.add(new LatLng(to.latitude, to.longitude));

        // hide last polyline
        if (mLastPolyline != null) mLastPolyline.setVisible(false);

        // prepare usage variables
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(Configs.GoogleMap.Polyline.WIDTH)
                .add(points.toArray(new LatLng[points.size()])));
//                .add(new LatLng(from.latitude, from.longitude),
//                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

        // keep last polyline
        mLastPolyline = polyline;

        // calculate kilometers
        distance += calculateTwoCoordinates(from, to);

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

        return distance;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
