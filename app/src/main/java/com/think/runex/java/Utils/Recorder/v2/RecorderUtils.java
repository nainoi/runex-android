package com.think.runex.java.Utils.Recorder.v2;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;

public class RecorderUtils {
    /**
     * Main variables
     */
    private final String ct = "RecorderUtils->";

    // instance variables
//    private Handler mRecursiveHandler;
//    private Runnable mRecursiveRunner;
    private onRecorderCallback mRecorderCallback;
//    private Activity mActivity;

    // explicit variables
    public long paceMillis = 0L;
    public double calories = 0L;
    public double distanceKm = 0.0;
    public double lastDistanceKm = 0.0;
    public String displayRecordAsTime = "00:00:00";
    public String displayPaceAsTime = "00:00:00";
    private boolean START = false;
    private final int RECURSIVE_TIME = 1000;
    private final int INCREATE_TIME = 1000;
    //--> stamp time recording
    private long stampMillis = 0L;
    public long recordDurationMillis = 0L;

    private RecorderUtils(/*Activity activity*/) {
        // prepare usage variables
        final String mtn = ct + "RecorderUtils->";

        // update props
//        this.mActivity = activity;
//        mRecursiveHandler = new Handler();
//        mRecursiveRunner = new Runnable() {
//            @Override
//            public void run() {
//                // recursive
//                recursive();
//
//            }
//        };

    }

    public static RecorderUtils newInstance(/*Activity activity*/) {
        return new RecorderUtils(/*activity*/);

    }

    /**
     * Process methods
     */
    private long diffMillisTime() {
        // prepare usage variables
        return System.currentTimeMillis() - stampMillis;
    }
//    private void recursive() {
//        mRecursiveHandler.postDelayed(mRecursiveRunner, RECURSIVE_TIME);
//
//    }

    /**
     * Feature methods
     */
    private void threadHandler() {
        // prepare usage variables
        final String mtn = ct + "threadHandler() ";

        // thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (START) {
                        // update record time'
                        recordDurationMillis += diffMillisTime();

                        //-> update props
                        stampMillis = System.currentTimeMillis();

                        // calculate cal
                        calories = calculateCalories();

                        // calculate pace
                        paceMillis = calculatePace();
                        // display time
                        displayPaceAsTime = DateTimeUtils.toTimeFormat(paceMillis);
                        displayRecordAsTime = DateTimeUtils.toTimeFormat(recordDurationMillis);

                        // callback
                        mRecorderCallback.onRecordTimeChanged(displayRecordAsTime);

                        // delayed
                        Thread.sleep(INCREATE_TIME);

//                        L.i(mtn + "cooking result...");
                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                }

            }
        }).start();
    }


    public double differenceDistance(xLocation from, xLocation to) {
        // calculate kilometers
        return calculateTwoCoordinates(from, to);
    }

    private double calculateTwoCoordinates(xLocation from, xLocation to) {
        // prepare usage variables
        final String mtn = ct + "calculateTwoCoordinates() ";
        double lat1 = from.latitude;
        double lat2 = to.latitude;
        double lng1 = from.longitude;
        double lng2 = to.longitude;
        double distance = (((Math.acos(Math.sin(lat1 * Math.PI / 180)
                * Math.sin(lat2 * Math.PI / 180) + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180)
                * Math.cos((lng1 - lng2) * Math.PI / 180))
                * 180 / Math.PI) * 60 * 1.1515) * 1.609344);

        // is NaN
        if (Double.isNaN(distance)) return 0;

        // return
        return distance;
    }

    public Double calculateCalories() {
        // prepare usage variables
        final String mtn = ct + "calculatePace() ";

        try {

            // distance does not changed
            if (lastDistanceKm == distanceKm || distanceKm <= 0)
                return calories;

            if (recordDurationMillis > 0) {
                // prepare usage variables
                final double prancerciseCaloriesPerHour = 450.00;
                final double hours = (recordDurationMillis / 1000.00) / 60.00 / 60.00;
                final double totalCalories = prancerciseCaloriesPerHour * hours;

                // update props
                return Double.isNaN(totalCalories) ? 0.00 : totalCalories;
            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return calories;
    }

    public long calculatePace() {
        // prepare usage variables
        final String mtn = ct + "calculatePace() ";

        try {
            // calculate pace
            final long millsec = (recordDurationMillis);

            if (distanceKm <= 0 || millsec <= 0 || Double.isNaN(millsec / 1000))
                return paceMillis;

            final double paceDulation = recordDurationMillis / (distanceKm);
            final int min = Integer.parseInt(((paceMillis / 1000) / 60) + "");
            final double secDuration = min * 60;
            final double sec = paceDulation - secDuration;
            final double pace = Double.parseDouble(min + "") + (sec / 1000);

            if (!Double.isNaN(pace)) {
                return (long) ((pace * 1000) > (20 * 3600 * 1000)
                        ? (20 * 3600 * 1000)
                        : pace * 1000);

            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        return paceMillis;
    }

    public void addDistance(double distance) {
        // prepare usage variables
        final String mtn = ct + "addDistance() ";
        distanceKm += distance;

    }

    public void setRecorderCallback(onRecorderCallback callback) {
        this.mRecorderCallback = callback;
    }

    public void reset() {
        // clear flag
        START = false;

        // clear result
        recordDurationMillis = 0L;
        paceMillis = 0L;
        displayRecordAsTime = "00:00:00";
        displayPaceAsTime = "00:00:00";
        distanceKm = 0.0;
        calories = 0.0;

    }

    public void start() {
        // prepare usage variables
        final String mtn = ct + "start() ";

        if (START) {
            // log
            L.i(mtn + "on recording...");

            // exit from this process
            return;
        }

        // update flag
        START = true;

        // update props
        stampMillis = System.currentTimeMillis();

        // thread handler
        threadHandler();

        // recursive
//        recursive();
    }

    public void resume() {

    }

    public void pause() {
        // stop recursive
//        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

    }

    public void stop() {

    }
}
