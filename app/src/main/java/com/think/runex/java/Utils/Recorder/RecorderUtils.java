package com.think.runex.java.Utils.Recorder;

import android.os.Handler;

import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.L;

public class RecorderUtils {
    /** Main variables */
    private final String ct = "RecorderUtils->";

    // instance variables
    private long mRecordTime = 0L;
    private Handler mRecursiveHandler;
    private Runnable mRecursiveRunner;
    private onRecorderCallback mRecorderCallback;

    // explicit variables
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

                // callback
                mRecorderCallback.onRecordTimeChanged(DateTimeUtils.instance().toTimeFormat( mRecordTime ));

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
    public void setRecorderCallback( onRecorderCallback callback){
        this.mRecorderCallback = callback;
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
