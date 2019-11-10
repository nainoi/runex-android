package com.think.runex.java.Utils.Network.Request;

import java.util.ArrayList;

public class rqSubmitMultiEvents {
    public double calory = 0.0;
    public double distance = 0.0;
    public String[] event_id = new String[0];
    public double time = 0.0;
    public String caption = null;

    public void setCalory(double calory) {
        this.calory = calory;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setEvent_id(String[] event_id) {
        this.event_id = event_id;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
