package com.think.runex.java.Utils;

import android.content.Context;
import android.os.Environment;

import com.think.runex.java.Constants.GPSRecordType;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.GPSFileRecordObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GPSFileRecorder {
    /** Main variables */
    private final String ct = "GPSFileRecorder->";

    // instance variables
    private FileOutputStream fileOps;
    private PrintWriter printWriter;
    private Context context;

    // explicit variables
    public final String EXTERNAL_PATH = Environment.getExternalStorageDirectory() +"/";
    public String fileName;

    public GPSFileRecorder(final String fileName, Context context){
        // update props
        this.fileName = fileName;
        this.context = context;

        // instance initialed;
        init() ;

    }

    /** Feature methods */
    public String readFile(){
        // prepare usage varaibles
        final String mtn = ct +"readFile() ";
        String returnString = null;

        try {
            // prepare usage variables
            InputStream ipsReader = new FileInputStream(EXTERNAL_PATH +"gps/"+ fileName);

            if( ipsReader != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(ipsReader);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //--> string receiver
                String stringReceiver = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (stringReceiver = bufferedReader.readLine()) != null ){
                    stringBuilder.append( stringReceiver );

                }

                inputStreamReader.close();
                returnString = stringBuilder.toString();

            } else L.e(mtn +"Err: ipsReader["+ ipsReader +"] is not ready." );

        } catch( Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }

        return returnString;
    }
    public GPSFileRecorder close(){
        // prepare usage variables
        final String mtn = ct +"close() ";

        try {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();

            }

            if (fileOps != null) {
                fileOps.close();
            }

        } catch ( Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }

        return this;
    }
    public GPSFileRecorder write(GPSFileRecordObject record, GPSRecordType type){
        // prepare usage variables
        final String mtn = ct +"write() ";

        try {
            printWriter.println(Globals.GSON.toJson(record) + type.SYMBOL);
            printWriter.flush();

        } catch ( Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }


        return this;
    }

    /** Init */
    private void init(){
        // prepare usage variables
        final String mtn = ct +"init() ";

        try {
            // prepare usage variables
            final String dirPath = EXTERNAL_PATH +"/gps";
            File gpsDirectory = new File(dirPath);

            // create new directory when
            // directory does not exists
            if( !gpsDirectory.exists() ){
                gpsDirectory.mkdirs();

            }

            // create file when
            // file does not exists
            File gpsFile = new File(dirPath +"/"+ fileName );

            if( !gpsFile.exists() ){
                L.i(mtn +"create new gps file.");
                gpsFile.createNewFile();

            }

            //--> crate output stream writer
            fileOps = new FileOutputStream(gpsFile, true);
            printWriter = new PrintWriter(fileOps);

        } catch ( Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }
    }
}
