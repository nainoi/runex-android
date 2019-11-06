package com.think.runex.java.Models;

public class RecorderObject {
    public double distanceKm = 0.0;
    public long recordingTime = 0L;
    public String recordingDisplayTime = "00:00:00";
    public int step = 0;
    public int calories = 0;

    public void setRecordingDisplayTime(String recordingDisplayTime) {
        this.recordingDisplayTime = recordingDisplayTime;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setRecordingTime(long recordingTime) {
        this.recordingTime = recordingTime;
    }
}
