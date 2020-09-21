package com.think.runex.java.Utils.GoogleMap;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class xLocation extends RealmObject implements Parcelable {

    public static final Creator<xLocation> CREATOR = new Creator<xLocation>() {
        @Override
        public xLocation createFromParcel(Parcel in) {
            return new xLocation(in);
        }

        @Override
        public xLocation[] newArray(int size) {
            return new xLocation[size];
        }
    };


    public double latitude = 0;
    public double longitude = 0;
    public double accuracy = 0;
    public double avgAccuracy = 0;

    public xLocation(){
    }

    public xLocation(double lat, double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

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

    protected xLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        accuracy = in.readDouble();
        avgAccuracy = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(accuracy);
        dest.writeDouble(avgAccuracy);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
