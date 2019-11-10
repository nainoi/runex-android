package com.think.runex.java.Constants;

public enum APIs {
    DOMAIN("https://runex.co:3006"),

    //--> A
    ADD_HISTORY(DOMAIN.VAL +"/api/v1/runhistory/add"),

    //--> G
    GET_REGISTERED_EVENT(DOMAIN.VAL + "/api/v1/register/myRegEvent"),
    GET_USER_PROFILE(DOMAIN.VAL + "/api/v1/user"),

    //--> L
    LOGIN(DOMAIN.VAL + "/api/v1/user/login"),

    //--> M
    MY_REGISTERED_EVENTS(DOMAIN.VAL + "/api/v1/register/myRegEvent"),

    //--> S
    SOCIALS_LOGIN(DOMAIN.VAL +"/api/v1/user/pd"),
    SUBMIT_RUNNING_RESULT(DOMAIN.VAL +"/api/v1/activity/add"),
    SOCIALS_LOGIN(DOMAIN.VAL + "/api/v1/user/pd"),
    ;

    public final String VAL;

    private APIs(String val) {
        this.VAL = val;

    }
}
