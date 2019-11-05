package com.think.runex.java.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class DeviceUtils {
    /** Main variables */
    private final String ct = "DeviceUtils->";

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

    public File takeScreenshot(Context context) {
        // prepare usage variables
        final String mtn = ct +"takeScreenshot() ";
        final String fileName = System.currentTimeMillis() +"";

        try {
            // path
            final String path = context.getExternalFilesDir(null).getAbsolutePath() +"/"+ fileName +".jpg";

            // create bitmap screen capture
            View root = ((Activity) context).getWindow().getDecorView()
                    .getRootView();
            root.setDrawingCacheEnabled(true);

            // create bitmap
            Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
            //--> drawing a cache
            root.setDrawingCacheEnabled(false);

            // create file temporary
            File imageFile = new File( path );

            // Write file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //--> flush and close stream writing
            outputStream.flush();
            outputStream.close();

            return imageFile;

        } catch (Throwable e) {
            L.e(mtn +"Err: "+ e.getMessage());

        }

        return null;
    }
}
