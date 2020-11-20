package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.think.runex.java.App.Configs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.think.runex.util.ConstantsKt.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH;


public class WorkoutInfo implements Parcelable {
    /**
     * id : 5dc02b9c874a693a72ef612e
     * distance : 0.01
     * img_url :
     * caption :
     * activity_date : 2019-11-04T13:46:04.662Z
     * created_at : 2019-11-04T13:46:04.741Z
     * updated_at : 2019-11-04T13:46:04.741Z
     */

    private String id;
    private String activity_type;
    private String app;
    private String ref_id;
    private double calory;
    private String caption;
    private double distance;
    private double pace;
    private int duration;
    private String time_string;
    private String start_date;
    private String end_date;
    private String workout_date;
    private int net_elevation_gain;
    private boolean is_sync;
    private List<RealmPointObject> locations;

    public WorkoutInfo() {
    }


    protected WorkoutInfo(Parcel in) {
        id = in.readString();
        activity_type = in.readString();
        app = in.readString();
        ref_id = in.readString();
        calory = in.readDouble();
        caption = in.readString();
        distance = in.readDouble();
        pace = in.readDouble();
        duration = in.readInt();
        time_string = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        workout_date = in.readString();
        net_elevation_gain = in.readInt();
        is_sync = in.readByte() != 0;
        locations = in.createTypedArrayList(RealmPointObject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(activity_type);
        dest.writeString(app);
        dest.writeString(ref_id);
        dest.writeDouble(calory);
        dest.writeString(caption);
        dest.writeDouble(distance);
        dest.writeDouble(pace);
        dest.writeInt(duration);
        dest.writeString(time_string);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(workout_date);
        dest.writeInt(net_elevation_gain);
        dest.writeByte((byte) (is_sync ? 1 : 0));
        dest.writeTypedList(locations);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkoutInfo> CREATOR = new Creator<WorkoutInfo>() {
        @Override
        public WorkoutInfo createFromParcel(Parcel in) {
            return new WorkoutInfo(in);
        }

        @Override
        public WorkoutInfo[] newArray(int size) {
            return new WorkoutInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public double getCalory() {
        return calory;
    }

    public void setCalory(double calory) {
        this.calory = calory;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTime_string() {
        return time_string;
    }

    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getWorkout_date() {
        return workout_date;
    }

    public String getWorkoutDate() {
        if (workout_date == null) {
            return "";
        }
        String changedDateTime = workout_date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Configs.SERVER_DATE_TIME_FORMAT, Locale.getDefault());
            Date date = sdf.parse(workout_date);
            if (date != null) {
                changedDateTime = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH, Locale.getDefault()).format(date);
            }
        } catch (Throwable error) {
            try {
                error.printStackTrace();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault());
                Date date = null;

                date = sdf.parse(workout_date);

                if (date != null) {
                    changedDateTime = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH, Locale.getDefault()).format(date);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return changedDateTime;
    }

    public void setWorkout_date(String workout_date) {
        this.workout_date = workout_date;
    }

    public int getNet_elevation_gain() {
        return net_elevation_gain;
    }

    public void setNet_elevation_gain(int net_elevation_gain) {
        this.net_elevation_gain = net_elevation_gain;
    }

    public boolean isIs_sync() {
        return is_sync;
    }

    public void setIs_sync(boolean is_sync) {
        this.is_sync = is_sync;
    }

    public List<RealmPointObject> getLocations() {
        return locations;
    }

    public void setLocations(List<RealmPointObject> locations) {
        this.locations = locations;
    }

    public List<LatLng> getLatLngList() {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (RealmPointObject point : locations) {
            latLngList.add(point.toLatLng());
        }
        return latLngList;
    }

    public RealmRecorderObject toRealmRecorderObject() {
        RealmRecorderObject object = new RealmRecorderObject();

        object.setWorkout_date(workout_date);
        object.setDistanceKm(distance);
        object.setDurationMillis((duration * 1000));
        object.setPaceMillis((long) ((pace * 60) * 1000));
        object.setDisplayRecordAsTime(time_string);
        //object.setDisplayPaceAsTime();
        object.setCalories(calory);

        return object;
    }
}
