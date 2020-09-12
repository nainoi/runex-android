package com.think.runex.java.Models;

import android.graphics.Bitmap;

import com.think.runex.java.Utils.GoogleMap.xLocation;

import java.io.Serializable;

import io.realm.RealmObject;

public class RealmRecorderObject extends RealmObject implements Serializable {
    public double distanceKm = 0.0;
    public long durationMillis = 0L;
    public long paceMillis = 0L;
    public String displayRecordAsTime = "00:00:00";
    public String displayPaceAsTime = "00:00:00";
    public double calories = 0;

    public xLocation xLoc;
    public xLocation xLocCurrent;
    public xLocation xLocLast;
    public boolean gpsAcquired = false;
    public boolean gpsPoorSignal = false;
    public boolean forceAction = false;

    public RealmRecorderObject() {

    }

    public void setDisplayPaceAsTime(String displayPaceAsTime) {
        this.displayPaceAsTime = displayPaceAsTime;
    }

    public void setDisplayRecordAsTime(String displayRecordAsTime) {
        this.displayRecordAsTime = displayRecordAsTime;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }
}
