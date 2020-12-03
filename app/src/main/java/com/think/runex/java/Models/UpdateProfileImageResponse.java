package com.think.runex.java.Models;

public class UpdateProfileImageResponse {
    private int code;
    private String msg;
    private UpdateProfileImageData data;

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

    public UpdateProfileImageData getData() {
        return data;
    }

    public void setData(UpdateProfileImageData data) {
        this.data = data;
    }
}
