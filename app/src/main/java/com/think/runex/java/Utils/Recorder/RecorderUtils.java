package com.think.runex.java.Utils.Recorder;

import android.app.Activity;
import android.os.Handler;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    public long mRecordTime = 0L;
    public long mRecordPace = 0L;
    public double mRecordCalories = 0L;
    public double mRecordDistanceKm = 0.0;
    public String mRecordDisplayTime = "00:00";
    public String mRecordPaceDisplayTime = "00:00";
    private boolean START = false;
    private final int RECURSIVE_TIME = 1000;
    private final int INCREATE_TIME = 1000;

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
                        mRecordTime += INCREATE_TIME;

                        // display time
                        mRecordDisplayTime = DateTimeUtils.toTimeFormat(mRecordTime);

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
            double toSec = mRecordTime / 1000;
            double toMin = toSec / 60;

            L.i(mtn + "record time: " + mRecordTime);
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
            final double toSec = mRecordPace / 1000;
            final double toMin = toSec / 60;

            if (mRecordPace <= 0 || Double.isNaN(toMin)) return 0;

            return toMin;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }

    public void calculatePace() {
        // prepare usage variables
        final String mtn = ct + "calculatePace() ";

        try {
            // calculate pace
            final long millsec = (mRecordTime);
            final double sec = millsec / 1000;

            if (mRecordDistanceKm <= 0 || sec <= 0 || Double.isNaN(sec)) return;

            if (!Double.isNaN(sec / mRecordDistanceKm)) {
                final long pace = Long.parseLong((long) (sec / mRecordDistanceKm) + "");

                if (!Double.isNaN(pace)) {
                    mRecordPace = (pace * 1000) > (20 * 3600 * 1000)
                            ? (20 * 3600 * 1000)
                            : pace * 1000;
                    mRecordPaceDisplayTime = DateTimeUtils.toTimeFormat(mRecordPace);

                }

            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }
    }

    public void addDistance(double distance) {
        // prepare usage variables
        final String mtn = ct + "addDistance() ";
        mRecordDistanceKm += distance;

        // calculate pace
        calculatePace();
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
        mRecordTime = 0L;
        mRecordPace = 0L;
        mRecordDisplayTime = "00:00";
        mRecordPaceDisplayTime = "00:00";
        mRecordDistanceKm = 0.0;

    }

    public void finish() {
        // stop recursive
        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

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
