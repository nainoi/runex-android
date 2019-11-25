package com.think.runex.java.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.think.runex.R;
import com.think.runex.java.Activities.BridgeFile;
import com.think.runex.java.Activities._RecordActivity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;

public class BackgroundService extends Service {
    private final String ct = "BackgroundService->";

    //--> Fused location provider client
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    final double FIXED_ACCURACY = 15;
    private boolean onDestroy = false;
    //--> notification
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    /**
     * Feature methods
     */
    private void broadcast(String jsonString) {
        Intent i = new Intent();
        i.setAction(Globals.BROADCAST_LOCATION);
        i.putExtra(Globals.BROADCAST_LOCATION_VAL, jsonString);

        sendBroadcast(i);
    }

    private void recursive(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if( onDestroy ) return;
//
                notificationBuilder.setContentText(System.currentTimeMillis() +"");
                notificationManager.notify(NOTIF_ID, notificationBuilder.build());

                recursive();
            }
        }, 1000);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        recursive();

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
            final String mtn = ct + "LocationCallback() ";

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location.getAccuracy() > FIXED_ACCURACY) return;
                    if (location != null) {
                        xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude()
                                , location.getAccuracy());

                        broadcast(Globals.GSON.toJson(xLoc));


//                        L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
                    }
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

        // display foreground
        startForeground();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        onDestroy = true;

        super.onDestroy();
        final String mtn = ct + "onDestroy() ";

        // remove location update
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        // log
        L.i(mtn + "");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
                .addAction(R.drawable.ic_record, "OK", pendingIntent)

                //--> custom
//                .setCustomContentView(mRemoteViews)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setOnlyAlertOnce(true)
                .setContentText("00000")
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
