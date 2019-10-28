package com.think.runex.java.Constants;

import android.Manifest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Globals {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzI0NTI2MTIsImlkIjoiNWRiNWJiZDE4NzRhNjkzYTcyZWY2MDkxIiwib3JpZ19pYXQiOjE1NzIxOTM0MTIsInBmIjoid2ViIiwicm9sZSI6Ik1FTUJFUiJ9.D1vXzTpIHiqeWIdUrwFgJxLJ_AuAJg2XCnKmtshWaqY";
    public static Gson GSON = new Gson();

    //--> Permission name
    public static final String ACCESS_FINE_LOCAITON = Manifest.permission.ACCESS_FINE_LOCATION;

    //--> Request code
    public static final int RC_BACK_FROM_LOGIN = 2201;
    public static final int RC_LOGIN_WITH_EMAIL = 2101;
    public static final int RC_LOGIN_WITH_GOOGLE = 2102;
    public static final int RC_LOGIN_WITH_FACEBOOK = 2103;

    //--> Permission request code
    public static final int RC_PERMISSION = 3101;
}
