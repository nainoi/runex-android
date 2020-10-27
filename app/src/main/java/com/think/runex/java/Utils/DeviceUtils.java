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

import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class DeviceUtils {
    /**
     * Main variables
     */
    private final String ct = "DeviceUtils->";

    // instance
    private static DeviceUtils ins;
    private Context context;
    private Point DISPLAY_SCREEN = null;

    // singleton
    private DeviceUtils(Context context) {
        this.context = context;
    }

    public static DeviceUtils instance(Context context) {
        return (ins == null) ? ins = new DeviceUtils(context) : ins;
    }

    /**
     * Feature methods
     */
    public Point getDisplaySize() {
        if (DISPLAY_SCREEN != null) return DISPLAY_SCREEN;

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


    public void openImagePicker(Activity activity, int requestCode) {
        _openImagePicker(activity, requestCode);

    }

    public void openImagePicker(Fragment fragment, int requestCode) {
        _openImagePicker(fragment, requestCode);
    }

    private void _openImagePicker(Object host, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        if (host instanceof Activity)
            (((Activity) host)).startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
        else if (host instanceof Fragment)
            (((Fragment) host)).startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    public boolean isDirectoryExists(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();

        }
        return true;
    }

    public File saveFileFromBitmap(Bitmap bitmap, String destinationPath) {
        // prepare usage variables
        final String mtn = ct + "saveFileFromBitmap() ";

        try {
            // create file temporary
            File file = new File(destinationPath);

            // Write file
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //--> flush and close stream writing
            outputStream.flush();
            outputStream.close();

            return file;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        return null;
    }

    public File takeScreenshot2(Context context, int viewId) {
        File outputFile = null;
        FileOutputStream outputStream = null;
        try {
            final String fileName = System.currentTimeMillis() + "";
            outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/RUNEX");
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            outputFile = new File(outputFile + "/" + fileName + ".jpg");
            outputStream = new FileOutputStream(outputFile);

            // create bitmap screen capture
            View frame = ((Activity) context).getWindow().getDecorView()
                    .getRootView();
            View root = frame.findViewById(viewId);
            root.setDrawingCacheEnabled(true);

            // create bitmap
            Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
            //--> drawing a cache
            root.setDrawingCacheEnabled(false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);


            //--> flush and close stream writing
            outputStream.flush();
            outputStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    public File takeScreenshot(Context context, int viewId) {
        // prepare usage variables
        final String mtn = ct + "takeScreenshot() ";
        final String fileName = System.currentTimeMillis() + "";

        try {
            // prepare usage variables
            final String dir = Environment.getExternalStorageDirectory() + "/RUNEX";
            isDirectoryExists(dir);

            // path
            final String path = dir + "/" + fileName + ".jpg";

            // create bitmap screen capture
            View frame = ((Activity) context).getWindow().getDecorView()
                    .getRootView();
            View root = frame.findViewById(viewId);
            root.setDrawingCacheEnabled(true);

            // create bitmap
            Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
            //--> drawing a cache
            root.setDrawingCacheEnabled(false);

            // save file
            return saveFileFromBitmap(bitmap, path);

        } catch (Throwable e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return null;
    }
}
