package com.think.runex.java.Models;

import io.realm.RealmObject;

public class RealmPointObject extends RealmObject {

    private Double latitude;
    private Double longitude;

    public RealmPointObject() {

    }

    public RealmPointObject(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
