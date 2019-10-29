package com.think.runex.java.Utils.Network.Response;

public class xResponse {
    public int responseCode = 0;
    public String jsonString;
    public String message;

    public xResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public xResponse setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public xResponse setJsonString(String jsonString) {
        this.jsonString = jsonString;
        return this;
    }
}
