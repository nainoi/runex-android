package com.think.runex.java.Utils.GoogleMap;

import java.io.Serializable;

public class xLocation implements Serializable {
    public double latitude = 0;
    public double longitude = 0;
    public double accuracy = 0;
    public double avgAccuracy = 0;

    public xLocation(double lat, double lng, double accuracy){
        this.accuracy = accuracy;
        this.latitude = lat;
        this.longitude = lng;
    }
    public xLocation(double lat, double lng, double accuracy, double averageAccuracy){
        this.accuracy = accuracy;
        this.avgAccuracy = averageAccuracy;
        this.latitude = lat;
        this.longitude = lng;
    }
    public xLocation(double lat, double lng){
        this.latitude = lat;
        this.longitude = lng;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
