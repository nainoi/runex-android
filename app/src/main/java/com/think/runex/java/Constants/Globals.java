package com.think.runex.java.Constants;

import android.Manifest;
import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.think.runex.java.App.Configs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Globals {
    //--> Activities
    public static final String ACTIVITY_RUN = "RUN";
    //--> Fixed
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzI0NTI2MTIsImlkIjoiNWRiNWJiZDE4NzRhNjkzYTcyZWY2MDkxIiwib3JpZ19pYXQiOjE1NzIxOTM0MTIsInBmIjoid2ViIiwicm9sZSI6Ik1FTUJFUiJ9.D1vXzTpIHiqeWIdUrwFgJxLJ_AuAJg2XCnKmtshWaqY";
    public static final String EVENT_ID = "5db5bda6874a693a72ef6093";
    public static Gson GSON = new Gson();
    public static DecimalFormat DCM = new DecimalFormat("0.0");
    public static DecimalFormat DCM_2 = new DecimalFormat("0.00");
    public static SimpleDateFormat SDF_DISPLAY_FULL_DATE_TIME = new SimpleDateFormat(Configs.SERVER_DISPLAY_FULL_DATE_TIME);
    public static SimpleDateFormat SDF = new SimpleDateFormat(Configs.SERVER_DATE_TIME_FORMAT);
    public static SimpleDateFormat SDF_TOKEN = new SimpleDateFormat(Configs.SERVER_TOKEN_DATE_TIME_FORMAT);
    public static SimpleDateFormat SDF_ONLY_DATE = new SimpleDateFormat(Configs.SERVER_DATE_FORMAT);
    public static final int FIXED_SCREEN_WIDTH = 1024;

    //--> Points
    //public static List<LatLng> POINTS;
    //--> Platform
    public static final String PLATFORM = "android";

    //--> Payment status
    public static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";

    //--> Auth
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";

    //--> Permission name
    public static final String ACCESS_FINE_LOCAITON = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    //--> Providers
    public static final String PROVIDER_GOOGLE = "GoogleID";

    //--> xRequest code
    public static final int RC_REFRESH = 902;
    public static final int RC_REGISTER_USER = 901;
    public static final int RC_RECORDER = 1001;
    public static final int RC_BACK_FROM_LOGIN = 2201;
    public static final int RC_NEED_LOGIN = 2100;
    public static final int RC_LOGIN_WITH_EMAIL = 2101;
    public static final int RC_LOGIN_WITH_GOOGLE = 2102;
    public static final int RC_LOGIN_WITH_FACEBOOK = 2103;
    public static final int RC_GALLERY_INTENT = 2104;
    public static final int RC_TO_PROFILE_PAGE = 2105;

    //--> Permission request code
    public static final int RC_PERMISSION = 3101;

    //--> Intent actions
    public static final int RC_PICK_IMAGE = 4100;

    //--> Broadcast
    public static final String JSON = "JSON";
    public static final String SERIALIZABLE = "SERIALIZABLE";
    public static final String BROADCAST_LOCATION = "BROADCAST";
    public static final String BROADCAST_SERVICE = "BROADCAST-SERVICE";
    public static final String BROADCAST_SERVICE_MANAGER = "BROADCAST-SERVICE-MANAGER";
    public static final String BROADCAST_TEST = "BROADCAST-TEST";
    public static final String BROADCAST_VAL = "BROADCAST-VAL";
    public static final String BROADCAST_LOCATION_VAL = "BROADCAST-LOCATION-VAL";
    public static final String BROADCAST_RECORD_VAL = "BROADCAST-RECORD-VAL";
    public static final String BROADCAST_ACTION_VAL = "BROADCAST-ACTION-VAL";

    //--> URL
    public static final String URL_RUNEX = "https://runex.co";

    public static String mapActivityResult(int activityResult) {
        if (activityResult == Activity.RESULT_OK) {
            return "RESULT_OK";

        } else if (activityResult == Activity.RESULT_CANCELED) {
            return "RESULT_CANCELED";

        } else return "NOTHING_MATCHED";

    }

}
