package com.think.runex.java.Utils.Recorder;

import android.app.Activity;
import android.os.Handler;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.L;

public class RecorderUtils {
    /**
     * Main variables
     */
    private final String ct = "RecorderUtils->";

    // instance variables
    private Handler mRecursiveHandler;
    private Runnable mRecursiveRunner;
    private onRecorderCallback mRecorderCallback;
    private Activity mActivity;

    // explicit variables
//    public long mRecordTime = 0L;
    public long paceMillis = 0L;
    public double calories = 0L;
    public double mRecordDistanceKm = 0.0;
    public double mLastRecordDistanceKm = 0.0;
    public String mRecordDisplayTime = "00:00:00";
    public String mRecordPaceDisplayTime = "00:00:00";
    private boolean START = false;
    private final int RECURSIVE_TIME = 1000;
    private final int INCREATE_TIME = 1000;
    //--> stamp time recording
    private long stampMillis = 0L;
    public long recordDurationMillis = 0L;

    private RecorderUtils(Activity activity) {
        // prepare usage variables
        final String mtn = ct + "RecorderUtils->";

        // update props
        this.mActivity = activity;
        mRecursiveHandler = new Handler();
        mRecursiveRunner = new Runnable() {
            @Override
            public void run() {
                // recursive
                recursive();

            }
        };

    }

    public static RecorderUtils newInstance(Activity activity) {
        return new RecorderUtils(activity);

    }

    /**
     * Process methods
     */
    private long diffMillisTime(){
        // prepare usage variables
        return System.currentTimeMillis() - stampMillis;
    }
    private void recursive() {
        mRecursiveHandler.postDelayed(mRecursiveRunner, RECURSIVE_TIME);

    }

    /**
     * Feature methods
     */
    private void threadHandler() {
        // prepare usage variables
        final String mtn = ct + "threadHandler() ";
        final Runnable mainRunner = new Runnable() {
            @Override
            public void run() {
                // callback
                mRecorderCallback.onRecordTimeChanged(mRecordDisplayTime);

            }


        };

        // thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (START) {
                        // update recording time
//                        mRecordTime += INCREATE_TIME;

                        // update record time'
                        recordDurationMillis += diffMillisTime();
                        //-> update props
                        stampMillis = System.currentTimeMillis();

                        // calculate cal
                        calculateCalories();

                        // calculate pace
                        calculatePace();

                        // display time
                        mRecordDisplayTime = DateTimeUtils.toTimeFormat(recordDurationMillis);

                        // main runner
                        mActivity.runOnUiThread(mainRunner);

                        // delayed
                        Thread.sleep(INCREATE_TIME);
                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                }

            }
        }).start();
    }

    public double recTimeAsMin() {
        // prepare usage variables
        final String mtn = ct + "recTimeAsMin() ";

        try {
            double toSec = recordDurationMillis / 1000;
            double toMin = toSec / 60;

            L.i(mtn + "record time: " + recordDurationMillis);
            L.i(String.format(mtn + "record time as min: " + toMin + ""));

            return toMin;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }

    public double paceAsMin() {
        // prepare usage variables
        final String mtn = ct + "paceAsMin() ";

        try {
            final double toSec = paceMillis / 1000;
            final double toMin = toSec / 60;

            if (paceMillis <= 0 || Double.isNaN(toMin)) return 0;

            return toMin;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }

    public void calculateCalories() {
        // prepare usage variables
        final String mtn = ct + "calculatePace() ";

        try {

            // distance does not changed
            if (mLastRecordDistanceKm == mRecordDistanceKm || mRecordDistanceKm <= 0) return;

            if (recordDurationMillis > 0) {
                // prepare usage variables
                final double prancerciseCaloriesPerHour = 450.00;
                final double hours = (recordDurationMillis / 1000.00) / 60.00 / 60.00;
                final double totalCalories = prancerciseCaloriesPerHour * hours;

                // update props
                calories = Double.isNaN(totalCalories) ? 0.00 : totalCalories;
            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

    }

    public void calculatePace() {
        // prepare usage variables
        final String mtn = ct + "calculatePace() ";

        try {
            // calculate pace
            final long millsec = (recordDurationMillis);

            if (mRecordDistanceKm <= 0 || millsec <= 0 || Double.isNaN(millsec / 1000)) return;

            final double paceDulation = recordDurationMillis / (mRecordDistanceKm);
            final int min = Integer.parseInt(((paceMillis / 1000) / 60) + "");
            final double secDuration = min * 60;
            final double sec = paceDulation - secDuration;
            final double pace = Double.parseDouble(min + "") + (sec / 1000);

            if (!Double.isNaN(pace)) {
                paceMillis = (long)((pace * 1000) > (20 * 3600 * 1000)
                        ? (20 * 3600 * 1000)
                        : pace * 1000);
                mRecordPaceDisplayTime = DateTimeUtils.toTimeFormat(paceMillis);

            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

    }

    public void addDistance(double distance) {
        // prepare usage variables
        final String mtn = ct + "addDistance() ";
        mRecordDistanceKm += distance;

    }

    public void setRecorderCallback(onRecorderCallback callback) {
        this.mRecorderCallback = callback;
    }

    public void reset() {
        // stop recursive
        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

        // clear result
        recordDurationMillis = 0L;
        paceMillis = 0L;
        mRecordDisplayTime = "00:00:00";
        mRecordPaceDisplayTime = "00:00:00";
        mRecordDistanceKm = 0.0;
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
        recursive();
    }

    public void resume() {

    }

    public void pause() {
        // stop recursive
        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

    }

    public void stop() {

    }
}
