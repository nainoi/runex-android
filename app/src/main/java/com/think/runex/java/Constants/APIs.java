package com.think.runex.java.Constants;

public enum  APIs {
    DOMAIN("https://runex.co:3006"),

    //--> A
    SUBMIT_RUNNING_RESULT(DOMAIN.VAL +"/api/v1/activity/add"),

    //--> G
    GET_REGISTERED_EVENT( DOMAIN.VAL +"/api/v1/register/myRegEvent"),
    GET_USER_PROFILE( DOMAIN.VAL +"/api/v1/user"),

    //--> L
    LOGIN(DOMAIN.VAL +"/api/v1/user/login"),

    //--> S
    SOCIALS_LOGIN(DOMAIN.VAL +"/api/v1/user/pd"),
    ;

    public final String VAL;
    private APIs(String val){
        this.VAL = val;

    }
}
