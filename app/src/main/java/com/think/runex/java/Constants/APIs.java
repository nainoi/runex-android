package com.think.runex.java.Constants;

public enum APIs {
    DOMAIN("https://runex.co:3006"),

    //--> A
    ADD_HISTORY(DOMAIN.VAL +"/api/v1/runhistory/add"),

    //--> G
    GET_EVENT_DETAIL(DOMAIN.VAL + "/api/v1/activity/getByEvent2/%s"), //{event_id}
    GET_REGISTERED_EVENT(DOMAIN.VAL + "/api/v1/register/myRegEvent"),
    GET_USER_PROFILE(DOMAIN.VAL + "/api/v1/user"),

    //--> L
    LOGIN(DOMAIN.VAL + "/api/v1/user/login"),
    LOGOUT(DOMAIN.VAL + "/api/v1/user/logout"),

    //--> M
    MY_RUNNING_HISTORY(DOMAIN.VAL + "/api/v1/runhistory/myhistory"),

    //--> R
    REGISTER_USER( DOMAIN.VAL+ "/api/v1/user/ep"),

    //--> S
    SOCIALS_LOGIN(DOMAIN.VAL +"/api/v1/user/pd"),
    SUBMIT_MULTI_EVENTS(DOMAIN.VAL + "/api/v1/activity/multiadd"),
    SUBMIT_RUNNING_RESULT(DOMAIN.VAL +"/api/v1/activity/add"),
    ;

    public final String VAL;

    private APIs(String val) {
        this.VAL = val;

    }
}
