package com.think.runex.java.Models;

public class TokenObject {

    /**
     * code : 200
     * expire : 2019-10-30T16:23:32Z
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzI0NTI2MTIsImlkIjoiNWRiNWJiZDE4NzRhNjkzYTcyZWY2MDkxIiwib3JpZ19pYXQiOjE1NzIxOTM0MTIsInBmIjoid2ViIiwicm9sZSI6Ik1FTUJFUiJ9.D1vXzTpIHiqeWIdUrwFgJxLJ_AuAJg2XCnKmtshWaqY
     */

    private int code;
    private String expire;
    private String token;
    private long expiredLong;

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
