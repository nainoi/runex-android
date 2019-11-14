package com.think.runex.java.Utils.Network.Response;

public class APIFormatResponse {

    /**
     * code : 401
     * msg : missing Username or Password
     */

    private int code;
    private String msg;

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
}
