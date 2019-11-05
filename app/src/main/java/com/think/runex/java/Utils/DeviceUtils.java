package com.think.runex.java.Utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtils {
    /** Main variables */

    // instance
    private static DeviceUtils ins;
    private Context context;
    private Point DISPLAY_SCREEN = null;

    // singleton
    private DeviceUtils( Context context){ this.context = context; }
    public static DeviceUtils instance(Context context){
        return (ins == null) ? ins = new DeviceUtils( context ) : ins;
    }

    /** Feature methods */
    public Point getDisplaySize(){
        if( DISPLAY_SCREEN != null ) return DISPLAY_SCREEN;

        // get display screen size
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // update
        DISPLAY_SCREEN = size;

        // return
        return DISPLAY_SCREEN;
    }
}
