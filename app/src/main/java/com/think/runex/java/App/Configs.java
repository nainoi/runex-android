package com.think.runex.java.App;


public class Configs {
    public static final String PLATFORM = "android";
    public static final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static class LocationTracker {
        public static float MIN_DISTANCE = 1.0f;
        public static long MIN_TIME = 1000L;
    }

    public static class GoogleMap {
        public static float INITIAL_ZOOM = 15f;
    }

}
