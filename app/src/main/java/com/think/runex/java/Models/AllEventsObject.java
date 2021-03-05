package com.think.runex.java.Models;

import com.think.runex.feature.event.data.EventItem;

import java.util.List;

public class AllEventsObject {

    private int code;
    private String msg;
    private List<EventItem> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<EventItem> getData() {
        return data;
    }

    public void setData(List<EventItem> data) {
        this.data = data;
    }
}
