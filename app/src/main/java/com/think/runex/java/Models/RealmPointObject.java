package com.think.runex.java.Models;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;

import static com.think.runex.util.ConstantsKt.DISPLAY_DATE_FORMAT;
import static com.think.runex.util.ConstantsKt.SERVER_DATE_TIME_FORMAT;

public class RealmPointObject extends RealmObject {

    private Double altitude;
    private Double elevation_gain = 0.0;
    private Double harth_rate = 0.0;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double temp = 0.0;
    private String timestamp = "";

    public RealmPointObject() {

    }

    public RealmPointObject(Double latitude, Double longitude, Double altitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = dateTimeFormat(System.currentTimeMillis());
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getElevation_gain() {
        return elevation_gain;
    }

    public void setElevation_gain(Double elevation_gain) {
        this.elevation_gain = elevation_gain;
    }

    public Double getHarth_rate() {
        return harth_rate;
    }

    public void setHarth_rate(Double harth_rate) {
        this.harth_rate = harth_rate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = dateTimeFormat(timestamp);
    }

    private String dateTimeFormat(Long timeStamp) {
        if (timeStamp <= 0) {
            return "";
        }
        String dateTime = "";
        try {
            dateTime = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).format(new Date(timeStamp));
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return dateTime;
    }


    public String getDate() {
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        }
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault());
            Date d = sdf.parse(timestamp);
            if (d != null) {
                date = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).format(d);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return date;
    }
}
