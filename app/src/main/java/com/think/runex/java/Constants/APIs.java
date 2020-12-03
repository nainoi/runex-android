package com.think.runex.java.Constants;

import com.think.runex.datasource.api.ApiConfig;

public enum APIs {
    //--> A
    ADD_HISTORY(ApiConfig.Companion.getBASE_URL() + "/api/v1/runhistory/add"),
    ADD_WORKOUT(ApiConfig.Companion.getBASE_URL() + "/api/v2/workout"),

    //--> D
    DELETE_EVENT_HISTORY(ApiConfig.Companion.getBASE_URL() + "/api/v1/activity/deleteActivity/%s/%s"), ///v1/activity/deleteActivity/{event_id}/{act_id}

    //--> E
    GET_ALL_EVENT(ApiConfig.Companion.getBASE_URL() + "/api/v2/event/active"),

    //--> G
    GET_EVENT_DETAIL(ApiConfig.Companion.getBASE_URL() + "/api/v2/activity/getByEvent2/%s"), //{event_id}
    GET_REGISTERED_EVENT(ApiConfig.Companion.getBASE_URL() + "/api/v2/register/myRegEvent"),
    GET_USER_PROFILE(ApiConfig.Companion.getBASE_URL() + "/api/v1/user"),

    //--> L
    LOGIN(ApiConfig.Companion.getBASE_URL() + "/api/v1/user/login"),
    LOGOUT(ApiConfig.Companion.getBASE_URL() + "/api/v2/logout"),

    //--> M
    MY_ACTIVE_REGISTERED_EVENT(ApiConfig.Companion.getBASE_URL() + "/api/v2/register/myRegEventActivate"),
    MY_RUNNING_HISTORY(ApiConfig.Companion.getBASE_URL() + "/api/v1/runhistory/myhistory"),
    MY_WORKOUTS(ApiConfig.Companion.getBASE_URL() + "/api/v2/workouts"),

    //--> R
    REGISTER_USER(ApiConfig.Companion.getBASE_URL() + "/api/v1/user/ep"),

    //--> S
    SOCIALS_LOGIN(ApiConfig.Companion.getBASE_URL() + "/api/v1/user/pd"),
    SUBMIT_MULTI_EVENTS(ApiConfig.Companion.getBASE_URL() + "/api/v1/activity/multiadd"),
    SUBMIT_RUNNING_RESULT(ApiConfig.Companion.getBASE_URL() + "/api/v1/activity/add"),
    SUBMIT_ACTIVITIES_WORKOUT(ApiConfig.Companion.getBASE_URL() + "/api/v2/activity/activitiesWorkout"),

    UPDATE_USER_PROFILE(ApiConfig.Companion.getBASE_URL() + "/api/v2/user"),
    UPDATE_USER_PROFILE_IMAGE(ApiConfig.Companion.getBASE_URL() + "/api/v2/uploads");


    public final String VAL;

    private APIs(String val) {
        this.VAL = val;

    }
}
