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

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public long getPaceMillis() {
        return paceMillis;
    }

    public void setPaceMillis(long paceMillis) {
        this.paceMillis = paceMillis;
    }

    public String getDisplayRecordAsTime() {
        return displayRecordAsTime;
    }

    public void setDisplayRecordAsTime(String displayRecordAsTime) {
        this.displayRecordAsTime = displayRecordAsTime;
    }

    public String getDisplayPaceAsTime() {
        return displayPaceAsTime;
    }

    public void setDisplayPaceAsTime(String displayPaceAsTime) {
        this.displayPaceAsTime = displayPaceAsTime;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public xLocation getxLoc() {
        return xLoc;
    }

    public void setxLoc(xLocation xLoc) {
        this.xLoc = xLoc;
    }

    public xLocation getxLocCurrent() {
        return xLocCurrent;
    }

    public void setxLocCurrent(xLocation xLocCurrent) {
        this.xLocCurrent = xLocCurrent;
    }

    public xLocation getxLocLast() {
        return xLocLast;
    }

    public void setxLocLast(xLocation xLocLast) {
        this.xLocLast = xLocLast;
    }

    public boolean isGpsAcquired() {
        return gpsAcquired;
    }

    public void setGpsAcquired(boolean gpsAcquired) {
        this.gpsAcquired = gpsAcquired;
    }

    public boolean isGpsPoorSignal() {
        return gpsPoorSignal;
    }

    public void setGpsPoorSignal(boolean gpsPoorSignal) {
        this.gpsPoorSignal = gpsPoorSignal;
    }

    public boolean isForceAction() {
        return forceAction;
    }

    public void setForceAction(boolean forceAction) {
        this.forceAction = forceAction;
    }
}
