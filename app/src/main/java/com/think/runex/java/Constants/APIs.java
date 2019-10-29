package com.think.runex.java.Constants;

public enum  APIs {
    DOMAIN("https://runex.co:3006"),

    //--> G
    GET_REGISTERED_EVENT( DOMAIN.VAL +"/api/v1/register/myRegEvent"),
    GET_USER_PROFILE( DOMAIN.VAL +"/api/v1/user"),

    //--> L
    LOGIN(DOMAIN.VAL +"/api/v1/user/login"),
    ;

    public final String VAL;
    private APIs(String val){
        this.VAL = val;

    }
}
