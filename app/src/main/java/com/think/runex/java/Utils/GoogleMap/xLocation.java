package com.think.runex.java.Utils.GoogleMap;

public class xLocation {
    public double latitude = 0;
    public double longitude = 0;

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
