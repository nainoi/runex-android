package com.think.runex.java.Models;

import com.think.runex.feature.event.model.registered.RegisteredEvent;

import java.util.ArrayList;
import java.util.List;

public class RegisteredEventsObject {

    private int code;
    private String msg;
    private ArrayList<RegisteredEvent> data;

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

    public ArrayList<RegisteredEvent> getData() {
        return data;
    }

    public void setData(ArrayList<RegisteredEvent> data) {
        this.data = data;
    }
}
