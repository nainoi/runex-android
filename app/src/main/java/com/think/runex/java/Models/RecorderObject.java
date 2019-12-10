package com.think.runex.java.Models;

import android.graphics.Bitmap;

import com.think.runex.java.Utils.GoogleMap.xLocation;

import java.io.Serializable;

public class RecorderObject implements Serializable {
    public double distanceKm = 0.0;
    public long recordingTime = 0L;
    public String displayRecordAsTime = "00:00";
    public String displayPaceAsTime = "00:00";
    public double calories = 0;
    public Bitmap mapPreviewImage;
    public xLocation xLoc;
    public xLocation xLocCurrent;
    public xLocation xLocLast;

    public void setMapPreviewImage(Bitmap mapPreviewImage) {
        this.mapPreviewImage = mapPreviewImage;
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

    public void setRecordingTime(long recordingTime) {
        this.recordingTime = recordingTime;
    }
}
