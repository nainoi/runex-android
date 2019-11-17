package com.think.runex.java.App;


public class Configs {
    public static final String PLATFORM = "android";
    public static final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String SERVER_TOKEN_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";

    public static class LocationTracker {
        public static float MIN_DISTANCE = .1f;
        public static long MIN_TIME = 500L;
    }

    public static class GoogleMap {
        public static float INITIAL_ZOOM = 18f;

        public static class Polyline {
            public static final String COLOR = "#FB6401";
            public static final float WIDTH = 35.0f;

        }
    }

}
