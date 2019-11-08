package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    public List<LatLng> points = new ArrayList<>();
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
    public void zoomToFit() {
        // exit from this process
        // when points are not ready
        // to do the fit
        if (points.size() <= 1) return;

        // prepare usage variables
        LatLng ll1 = points.get(0);
        LatLng ll2 = points.get(points.size() - 1);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // scope
        builder.include(ll1);
        builder.include(ll2);
        builder.include(new LatLng(ll1.latitude - 0.006, ll1.longitude - 0.006));
        builder.include(new LatLng(ll2.latitude + 0.006, ll2.longitude + 0.006));
        LatLngBounds bounds = builder.build();

        // move camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));

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

    public void addDistance(xLocation from, xLocation to) {
        distance += difDistance(from, to);
    }

    public double difDistance(xLocation from, xLocation to) {
        // calculate kilometers
        return calculateTwoCoordinates(from, to);
    }

    public void addPolyline(xLocation from, xLocation to) {
        // start point
        if (points.size() <= 0) points.add(new LatLng(from.latitude, from.longitude));

        // end point
        points.add(new LatLng(to.latitude, to.longitude));

        // prepare usage variables
        final float polyWidth = mMap.getCameraPosition().zoom % 3 == 0
                ? mMap.getCameraPosition().zoom / 3
                : 5;
        final int color = Color.parseColor(Configs.GoogleMap.Polyline.COLOR);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .width(15)
                .add(points.toArray(new LatLng[points.size()])));
//                .add(new LatLng(from.latitude, from.longitude),
//                        new LatLng(to.latitude, to.longitude)));
        polyline.setColor(color);

        // hide last polyline
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
