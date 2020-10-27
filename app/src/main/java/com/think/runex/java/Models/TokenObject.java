package com.think.runex.java.Models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DateTime.DisplayDateTimeObject;
import com.think.runex.java.Utils.L;

import java.net.HttpURLConnection;

public class TokenObject {
    // prepare usage variables
    private final String ct = "TokenObject->";
    /**
     * code : 200
     * expire : 2019-10-30T16:23:32Z
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzI0NTI2MTIsImlkIjoiNWRiNWJiZDE4NzRhNjkzYTcyZWY2MDkxIiwib3JpZ19pYXQiOjE1NzIxOTM0MTIsInBmIjoid2ViIiwicm9sZSI6Ik1FTUJFUiJ9.D1vXzTpIHiqeWIdUrwFgJxLJ_AuAJg2XCnKmtshWaqY
     */

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String message;
    @SerializedName("expire")
    private String expire;
    @SerializedName("token")
    private String token;

    public boolean isAlive() {
        // prepare usage variables
        final String mtn = ct + "isAlive() ";
        final long current = System.currentTimeMillis() / 1000;
        final long expired = getExpiredLong();

        L.i(mtn + "//---> Is Token Alive");
        L.i(mtn + "expire-timestamp: " + expired);
        L.i(mtn + "current-timestamp: " + current);
        L.i(mtn + "remaining as second: " + ((expired - current)));
        L.i(mtn + "");

        return !TextUtils.isEmpty(token);
    }

    public void printInfo() {
        // prepare usage variables
        final String mtn = ct + "printInfo() ";

        L.i("");
        L.i(mtn + "* * * Token * * *");
        L.i(mtn + "expire: " + expire);
        L.i(mtn + "expire-long: " + getExpiredLong());
        L.i(mtn + "token: " + token);

        // validate token
        isAlive();

        L.i("");
        L.i("");
    }

    public long getExpiredLong() {
        if (expire == null || expire.length() == 0) {
            return 0;
        }
        DisplayDateTimeObject displayDate = DateTimeUtils.stringToDate(expire, Configs.SERVER_TOKEN_DATE_TIME_FORMAT);
        return displayDate.timestamp;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccessFul() {
        return code == HttpURLConnection.HTTP_OK;
    }
}
