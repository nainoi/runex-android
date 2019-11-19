package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_FORMAT;

public class ActivityInfoBean implements Parcelable {
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
    private double distance;
    private String img_url;
    private String caption;
    private String activity_date;
    private String created_at;
    private String updated_at;
    //--> custom date
    private String custom_display_date = "-";

    public ActivityInfoBean() {
    }

    protected ActivityInfoBean(Parcel in) {
        id = in.readString();
        distance = in.readDouble();
        img_url = in.readString();
        caption = in.readString();
        activity_date = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(distance);
        dest.writeString(img_url);
        dest.writeString(caption);
        dest.writeString(activity_date);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityInfoBean> CREATOR = new Creator<ActivityInfoBean>() {
        @Override
        public ActivityInfoBean createFromParcel(Parcel in) {
            return new ActivityInfoBean(in);
        }

        @Override
        public ActivityInfoBean[] newArray(int size) {
            return new ActivityInfoBean[size];
        }
    };

    public String getCustom_display_date() {
        return custom_display_date;
    }

    public void setCustom_display_date(String custom_display_date) {
        this.custom_display_date = custom_display_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getActivityDate() {
        if (activity_date == null || activity_date.length() == 0) return activity_date;
        try {
            int lastIndex = activity_date.lastIndexOf(":");
            String activityDate = activity_date.substring(0, lastIndex);
            String serverPattern = "yyyy-MM-dd'T'HH:mm";

            Date date = new SimpleDateFormat(serverPattern, Locale.getDefault()).parse(activityDate);
            if (date != null) {
                return new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).format(date);
            } else {
                return activityDate;
            }
        } catch (Throwable error) {
            error.printStackTrace();
            return activity_date;
        }

    }
}
