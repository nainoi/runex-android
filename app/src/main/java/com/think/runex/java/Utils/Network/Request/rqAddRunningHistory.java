package com.think.runex.java.Utils.Network.Request;

public class rqAddRunningHistory  {


    /**
     * activity_type : RUN
     * caption :
     * time : 25.4287428855896
     * image_path : /upload/image/event/544eef65-29ef-49bb-b023-5988c59dece8.png
     * calory : 3.1785928606987
     * distance : 99.93218236146105
     * pace : 3.98012973396608
     */

    private String activity_type;
    private String caption;
    private double time;
    private String image_path;
    private double calory;
    private double distance;
    private double pace;

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public double getCalory() {
        return calory;
    }

    public void setCalory(double calory) {
        this.calory = calory;
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
}
