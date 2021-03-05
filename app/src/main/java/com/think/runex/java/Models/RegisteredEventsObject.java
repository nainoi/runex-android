package com.think.runex.java.Models;

import com.think.runex.feature.event.data.EventRegistered;

import java.util.ArrayList;

public class RegisteredEventsObject {

    private int code;
    private String msg;
    private ArrayList<EventRegistered> data;

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

    public ArrayList<EventRegistered> getData() {
        return data;
    }

    public void setData(ArrayList<EventRegistered> data) {
        this.data = data;
    }
}
