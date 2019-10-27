package com.think.runex.java.Constants;

public enum  APIs {
    DOMAIN("https://runex.co:3006"),
    GET_REGISTERED_EVENT( DOMAIN.VAL +"/api/v1/register/myRegEvent");

    public final String VAL;
    private APIs(String val){
        this.VAL = val;

    }
}
