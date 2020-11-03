package com.think.runex.java.Models;

import com.think.runex.feature.event.model.registered.RegisteredEvent;

import java.util.List;

public class RegisteredEventsObject {

    private int code;
    private String msg;
    private List<RegisteredEvent> data;

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

    public List<RegisteredEvent> getData() {
        return data;
    }

    public void setData(List<RegisteredEvent> data) {
        this.data = data;
    }
}
