package com.think.runex.java.Utils;

import android.util.Log;

import com.think.runex.java.Constants.Constants;

public class L {
    public static void i(String msg){
        Log.i(Constants.TAG.VAL, msg);

    }

    public static void e(String msg){
        Log.e(Constants.TAG.VAL, msg);

    }
}
