package com.think.runex.java.Utils.Network.Request;

import com.think.runex.java.Models.RealmPointObject;

import java.util.ArrayList;
import java.util.List;

import static com.think.runex.config.ConstantsKt.ANDROID;

public class rqAddWorkOutsHistory {

    /**
     {
     "activity_type": "run1",
     "app": "RUNEX",
     "calory": 100,
     "caption": "test posman",
     "distance": 9.5,
     "duration": 3125,
     "time_string": "00:52:05",
     "end_date": "2020-09-02T00:00:00Z",
     "is_sync": true,
     "locations": [
     {
     "altitude": 0,
     "elevation_gain": 0,
     "harth_rate": 0,
     "latitude": 0,
     "longitude": 0,
     "temp": 0,
     "timestamp": "2020-09-02T00:00:00Z"
     }
     ],
     "net_elevation_gain": 0,
     "pace": 7,
     "start_date": "2020-09-02T00:00:00Z",
     "workout_date": "2020-09-02T00:00:00Z"
     }

     {
     "activity_type": "run1",
     "app": "RUNEX",
     "calory": 1.9015,
     "caption": "",
     "distance": 0.1396175146042612,
     "duration": 15,
     "end_date": "2020-10-27T00:53:17Z",
     "is_sync": false,
     "locations": [
     {
     "altitude": 20,
     "elevation_gain": 0,
     "harth_rate": 0,
     "latitude": 16.4338706,
     "longitude": 102.8110751,
     "temp": 0,
     "timestamp": "2020-10-27T00:53:07Z"
     }
     ],
     "net_elevation_gain": 0,
     "pace": 1.8166666666666667,
     "start_date": "2020-10-27T00:53:07Z",
     "time_string": "00:00:15",
     "workout_date": "2020-10-27T00:53:07Z"
     }
     */

    private String activity_type = "run1";
    private String app = "RUNEX";
    private double calory = 0.0;
    private String caption = "";
    private double distance = 0.0;
    private long duration = 0;
    private String time_string = "";
    private String end_date = "";
    private Boolean is_sync = false;
    private List<RealmPointObject> locations = new ArrayList<>();
    private Double net_elevation_gain = 0.0;
    private Double pace = 0.0;
    private String start_date = "";

    private String workout_date = "";

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

    public double getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<RealmPointObject> getLocations() {
        return locations;
    }

    public void setLocations(List<RealmPointObject> locations) {
        this.locations = locations;
    }

    public Double getNet_elevation_gain() {
        return net_elevation_gain;
    }

    public void setNet_elevation_gain(Double net_elevation_gain) {
        this.net_elevation_gain = net_elevation_gain;
    }

    public Double getPace() {
        return pace;
    }

    public void setPace(Double pace) {
        this.pace = pace;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getTime_string() {
        return time_string;
    }

    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }

    public String getWorkout_date() {
        return workout_date;
    }

    public void setWorkout_date(String workout_date) {
        this.workout_date = workout_date;
    }

    public Boolean getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(Boolean is_sync) {
        this.is_sync = is_sync;
    }
}
