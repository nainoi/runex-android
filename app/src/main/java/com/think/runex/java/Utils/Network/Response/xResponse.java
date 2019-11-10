package com.think.runex.java.Utils.Network.Response;

public class xResponse {
    public int responseCode = 0;
    public String jsonString;
    public String message;
    public String url;

    public void setUrl(String url) {
        this.url = url;
    }

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
