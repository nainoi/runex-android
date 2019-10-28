package com.think.runex.java.Utils.GoogleMap;

import android.app.Activity;

public class GoogleMapUtils {
    /** Main variables */
    private final String ct = "GoogleMapUtils->";

    // instance variables
    private Activity mActivity;

    private GoogleMapUtils(Activity activity){
        this.mActivity = activity;
    }
    public static GoogleMapUtils newInstance(Activity activity){
        return new GoogleMapUtils( activity );
    }


    /** Feature methods */
    public void addPolyline(){
        
    }

}
