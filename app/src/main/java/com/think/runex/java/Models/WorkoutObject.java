package com.think.runex.java.Models;

public class WorkoutObject {
    private int code;
    private String msg;
    private WorkoutInfo data;

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

    public WorkoutInfo getData() {
        return data;
    }

    public void setData(WorkoutInfo data) {
        this.data = data;
    }
}
