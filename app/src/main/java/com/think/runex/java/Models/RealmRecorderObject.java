package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.think.runex.java.Utils.GoogleMap.xLocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;

import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_FORMAT;
import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH;
import static com.think.runex.config.ConstantsKt.SERVER_DATE_TIME_FORMAT;

public class RealmRecorderObject extends RealmObject implements Parcelable {

    public static final Creator<RealmRecorderObject> CREATOR = new Creator<RealmRecorderObject>() {
        @Override
        public RealmRecorderObject createFromParcel(Parcel in) {
            return new RealmRecorderObject(in);
        }

        @Override
        public RealmRecorderObject[] newArray(int size) {
            return new RealmRecorderObject[size];
        }
    };

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

    private String workout_date = "";

    public RealmRecorderObject() {
    }

    protected RealmRecorderObject(Parcel in) {
        distanceKm = in.readDouble();
        durationMillis = in.readLong();
        paceMillis = in.readLong();
        displayRecordAsTime = in.readString();
        displayPaceAsTime = in.readString();
        calories = in.readDouble();
        xLoc = in.readParcelable(xLocation.class.getClassLoader());
        xLocCurrent = in.readParcelable(xLocation.class.getClassLoader());
        xLocLast = in.readParcelable(xLocation.class.getClassLoader());
        gpsAcquired = in.readByte() != 0;
        gpsPoorSignal = in.readByte() != 0;
        forceAction = in.readByte() != 0;
        workout_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(distanceKm);
        dest.writeLong(durationMillis);
        dest.writeLong(paceMillis);
        dest.writeString(displayRecordAsTime);
        dest.writeString(displayPaceAsTime);
        dest.writeDouble(calories);
        dest.writeParcelable(xLoc, flags);
        dest.writeParcelable(xLocCurrent, flags);
        dest.writeParcelable(xLocLast, flags);
        dest.writeByte((byte) (gpsAcquired ? 1 : 0));
        dest.writeByte((byte) (gpsPoorSignal ? 1 : 0));
        dest.writeByte((byte) (forceAction ? 1 : 0));
        dest.writeString(workout_date);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getWorkout_date() {
        return workout_date;
    }

    public void setWorkout_date(String workout_date) {
        this.workout_date = workout_date;
    }

    public String getWorkoutDate() {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault());
            Date d = sdf.parse(workout_date);
            if (d != null) {
                date = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH, Locale.getDefault()).format(d);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return date;
    }

    public void setWorkoutDate(long timeMillis) {
        try {
            Date d = new Date(timeMillis);
            workout_date = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).format(d);
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }
}
