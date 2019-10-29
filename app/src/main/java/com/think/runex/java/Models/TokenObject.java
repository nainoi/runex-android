package com.think.runex.java.Models;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DateTime.DisplayDateTimeObject;
import com.think.runex.java.Utils.L;

import java.util.Calendar;
import java.util.Date;

public class TokenObject {
    // prepare usage variables
    private final String ct = "TokenObject->";
    /**
     * code : 200
     * expire : 2019-10-30T16:23:32Z
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzI0NTI2MTIsImlkIjoiNWRiNWJiZDE4NzRhNjkzYTcyZWY2MDkxIiwib3JpZ19pYXQiOjE1NzIxOTM0MTIsInBmIjoid2ViIiwicm9sZSI6Ik1FTUJFUiJ9.D1vXzTpIHiqeWIdUrwFgJxLJ_AuAJg2XCnKmtshWaqY
     */

    private int code;
    private String expire;
    private String token;
    private long expiredLong;


    /** Feature methods */
    public void init(){
        DisplayDateTimeObject displayDate = DateTimeUtils.instance().stringToDate( expire );
        expiredLong =  displayDate.timestamp;
    }
    public boolean isAlive(){
        // prepare usage variables
        final String mtn = ct +"isAlive() ";
        final long current = System.currentTimeMillis() / 1000;

        L.i(mtn +"//---> Is Token Alive");
        L.i(mtn +"expire-timestamp: "+ expiredLong);
        L.i(mtn +"current-timestamp: "+ current);
        L.i(mtn +"remaining-timestamp: "+ (expiredLong - current));
        L.i(mtn +"");

        return current <= expiredLong;
    }
    public void printInfo(){
        // prepare usage variables
        final String mtn = ct +"printInfo() ";

        L.i("");
        L.i(mtn +"* * * Token * * *");
        L.i(mtn +"expire: "+ expire);
        L.i(mtn +"expire-long: "+ expiredLong);
        L.i(mtn +"token: "+ token);

        // validate token
        isAlive();

        L.i("");
        L.i("");
    }

    public long getExpiredLong() {
        return expiredLong;
    }

    public void setExpiredLong(long expiredLong) {
        this.expiredLong = expiredLong;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
}
