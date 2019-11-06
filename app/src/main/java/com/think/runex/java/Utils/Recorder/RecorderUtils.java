package com.think.runex.java.Utils.Recorder;

import android.os.Handler;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;

import java.util.ArrayList;
import java.util.List;

public class RecorderUtils {
    /** Main variables */
    private final String ct = "RecorderUtils->";

    // instance variables
    private Handler mRecursiveHandler;
    private Runnable mRecursiveRunner;
    private onRecorderCallback mRecorderCallback;

    // explicit variables
    public long mRecordTime = 0L;
    public double mRecordDistanceKm = 0.0;
    public String mRecordDisplayTime = "00:00:00";
    private boolean START = false;
    private final int RECURSIVE_TIME = 1000;
    private final int INCREATE_TIME = 1000;

    private RecorderUtils(){
        // prepare usage variables
        final String mtn = ct +"RecorderUtils->";

        // update props
        mRecursiveHandler = new Handler();
        mRecursiveRunner = new Runnable(){
            @Override
            public void run() {
                // update recording time
                mRecordTime += INCREATE_TIME;

                // display time
                mRecordDisplayTime = DateTimeUtils.instance().toTimeFormat( mRecordTime );

                // callback
                mRecorderCallback.onRecordTimeChanged( mRecordDisplayTime );

                // recursive
                recursive();

            }
        };

    }
    public static RecorderUtils newInstance(){
        return new RecorderUtils();

    }

    /** Process methods */
    private void recursive(){
        mRecursiveHandler.postDelayed(mRecursiveRunner, RECURSIVE_TIME);

    }

    /** Feature methods */
    public void addDistance( double distance){
        mRecordDistanceKm += distance;
    }
    public void setRecorderCallback( onRecorderCallback callback){
        this.mRecorderCallback = callback;
    }
    public void reset(){
        // stop recursive
        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

        // clear result
        mRecordTime = 0L;
        mRecordDisplayTime = "00:00:00";
        mRecordDistanceKm = 0.0;

    }
    public void finish(){
        // stop recursive
        mRecursiveHandler.removeCallbacksAndMessages(null);

        // clear flag
        START = false;

    }
    public void start() {
        // prepare usage variables
        final String mtn = ct +"start() ";

        if( START ){
            // log
            L.i(mtn +"on recording...");

            // exit from this process
            return;
        }

        // update flag
        START = true;

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
