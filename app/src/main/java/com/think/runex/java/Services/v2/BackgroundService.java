package com.think.runex.java.Services.v2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.think.runex.R;
import com.think.runex.java.Activities.BridgeFile;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;
import com.think.runex.java.Utils.Recorder.v2.RecorderUtils;

public class BackgroundService extends Service {
    private final String ct = "BackgroundService->";

    //--> Fused location provider client
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    final double FIXED_ACCURACY = 15;
    final double FIXED_ACQUIRING = 3;
    private int acquiring_count = 0;
    private boolean onDestroy = false;
    private xLocation lastLocation = null;
    //--> notification
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    //--> Recorder
    private RecorderUtils recorderUtils;
    //--> broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // prepare usage variables
        final String mtn = ct + "BroadcastReceiver() ";

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                L.i(mtn + "received action...");

            } catch (Exception e) {
                e.printStackTrace();
                L.e(mtn + "Err: " + e.getMessage());
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        final String mtn = ct + "onCreate() ";

        // register broadcast
        registerBroadcast();
//        recursive();

        // initial recorder utils
        recorderUtils = RecorderUtils.newInstance();
        recorderUtils.setRecorderCallback(new onRecorderCallback() {
            @Override
            public void onRecordTimeChanged(String time) {
                // when last location
                // is not ready
                if( lastLocation == null ) return;

                // prepare usage variables
                Intent i = new Intent();
                RecorderObject record = new RecorderObject();

                //--> record props
                record.displayRecordAsTime = recorderUtils.displayRecordAsTime;
                record.displayPaceAsTime = recorderUtils.displayPaceAsTime;
                record.displayPaceAsTime = recorderUtils.displayPaceAsTime;
                record.distanceKm = recorderUtils.distanceKm;
                record.calories = recorderUtils.calories;
                record.xLocCurrent = new xLocation(lastLocation.latitude, lastLocation.longitude);

                //--> intent props
                i.setAction(Globals.BROADCAST_TEST);
                i.putExtra(Globals.BROADCAST_LOCATION_VAL, Globals.GSON.toJson(record));

                // send broadcast
                sendBroadcast(i);

            }
        });
        recorderUtils.start();

        // update props
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            // prepare usage variables
            final String mtn = ct + "getLastLocation() ";

            @Override
            public void onSuccess(Location location) {
                if (location != null && location.getAccuracy() <= FIXED_ACCURACY) {
                    xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude());

//                    broadcast( Globals.GSON.toJson( xLoc ) );
//
//                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
                }
            }
        });

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1);
        mLocationRequest.setInterval(1 * 1000);
        mLocationRequest.setFastestInterval(1 * 1000);
        mLocationCallback = new LocationCallback() {
            // prepare usage variables
            final String mtn = ct + "LocationCallback() - test ";

            @Override
            public void onLocationResult(LocationResult locationResult) {
                // location is not ready
                if (locationResult == null) return;

                // loop locations
                for (Location location : locationResult.getLocations()) {
                    // useless location
                    if (location == null || location.getAccuracy() > (FIXED_ACCURACY)) return;

                    // acquiring
                    if (acquiring_count < FIXED_ACQUIRING) ++acquiring_count;

                    // accepted location
                    xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude()
                            , location.getAccuracy());

                    // broadcast
                    broadcast(Globals.GSON.toJson(xLoc));

                    // last location condition
                    if (lastLocation == null) {
                        // log
                        L.i(mtn + "update last location.");

                        // update last location;
                        lastLocation = xLoc;

                        // exit from this process
                        return;
                    }

                    // difference distance
                    final double differenceDist = recorderUtils.differenceDistance(xLoc, lastLocation);

                    // should add distance
                    if (differenceDist > 0.005) {
                        // update distance
                        recorderUtils.addDistance(differenceDist);


                    }

                    // update last location
                    lastLocation = xLoc;

                    L.i(mtn + "");
                    L.i(mtn + "");
                    L.i(mtn + " * * * Location Information * * * ");
                    L.i(mtn + "--> timing");
                    L.i(mtn + "Pace: " + recorderUtils.displayPaceAsTime);
                    L.i(mtn + "Record duration: " + recorderUtils.displayRecordAsTime);
                    L.i(mtn + "--> Unit");
                    L.i(mtn + "Calorie: " + recorderUtils.calories);
                    L.i(mtn + "Distance: " + recorderUtils.distanceKm);
                    L.i(mtn + "--> Ect.");
                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
                    L.i(mtn + "");
                    L.i(mtn + "");
                    L.i(mtn + "");

                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

        // display foreground
        startForeground();

    }


    /**
     * Feature methods
     */
    private void unregisterBroadcast() {
        unregisterReceiver(broadcastReceiver);
    }

    private void registerBroadcast() {
        registerReceiver(broadcastReceiver, new IntentFilter(Globals.BROADCAST_SERVICE));

    }

    private void locationUpdate(xLocation loc) {
    }

    private void broadcast(String jsonString) {
        Intent i = new Intent();
        i.setAction(Globals.BROADCAST_LOCATION);
        i.putExtra(Globals.BROADCAST_LOCATION_VAL, jsonString);

        sendBroadcast(i);
    }

    private void recursive() {
        new Handler().postDelayed(new Runnable() {
            // prepare usage variables
            final String mtn = ct + "recursive() ";

            @Override
            public void run() {
                //if (onDestroy) return;
//
                if (notificationBuilder != null) {
                    notificationBuilder.setContentText(System.currentTimeMillis() + "");
                    notificationManager.notify(NOTIF_ID, notificationBuilder.build());

                }

                L.i(mtn + "recursive: " + System.currentTimeMillis());
                recursive();
            }
        }, 1000);
    }

    /**
     * Life cycle
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        onDestroy = true;

        // unregister broadcast
        unregisterBroadcast();

        super.onDestroy();
        final String mtn = ct + "onDestroy() ";

        // remove location update
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        // log
        L.i(mtn + "");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Notification feature
     */
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    private void startForeground() {
        // prepare usage variables
        Intent notificationIntent = new Intent(this, BridgeFile.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create notification channel
        createNotificationChannel();

//        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_lay);
        Notification notification = (notificationBuilder = new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                //--> display
                .setSmallIcon(R.drawable.ic_logo_small)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                //--> actions
//                .addAction(R.drawable.ic_record, "OK", pendingIntent)

                //--> custom
//                .setCustomContentView(mRemoteViews)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentText("Background service is running.")
                .setOnlyAlertOnce(true)
//                .setContentText("00000")
//                .setStyle(new NotificationCompat.InboxStyle())

                //--> contents
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)

                //--> create build
        ).build();

        // start foreground notification
        startForeground(NOTIF_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
