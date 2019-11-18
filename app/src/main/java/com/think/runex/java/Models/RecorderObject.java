package com.think.runex.java.Models;

import android.graphics.Bitmap;

public class RecorderObject {
    public double distanceKm = 0.0;
    public long recordingTime = 0L;
    public String recordingDisplayTime = "00:00";
    public String recordingPaceDisplayTime = "00:00";
    public double calories = 0;
    public Bitmap mapPreviewImage;

    public void setMapPreviewImage(Bitmap mapPreviewImage) {
        this.mapPreviewImage = mapPreviewImage;
    }

    public void setRecordingPaceDisplayTime(String recordingPaceDisplayTime) {
        this.recordingPaceDisplayTime = recordingPaceDisplayTime;
    }

    public void setRecordingDisplayTime(String recordingDisplayTime) {
        this.recordingDisplayTime = recordingDisplayTime;
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
