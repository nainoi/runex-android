package com.think.runex.java.Services.v3;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.think.runex.R;
import com.think.runex.java.Activities.BridgeFile;
import com.think.runex.java.Constants.BroadcastAction;
import com.think.runex.java.Constants.BroadcastType;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.BackgroundServiceInfoObject;
import com.think.runex.java.Models.BroadcastObject;
import com.think.runex.java.Models.DebugUIObject;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;
import com.think.runex.java.Utils.Recorder.v2.RecorderUtils;

import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service {
    private final String ct = "BackgroundService->";

    //--> Fused location provider client
    FusedLocationProviderClient mFusedLocationClient;
    //--> Location manager
    LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private xLocation lastLocation = null;
    private RecorderObject currentRecorder = null;
    private List<LatLng> points = new ArrayList<>();
    //--> Flags
    private boolean onRecordPaused = false;
    private boolean onRecordStarted = false;
    private boolean onDestroy = false;
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
                // prepare usage variables
                final BroadcastObject broadcast = (BroadcastObject) intent.getSerializableExtra(Globals.SERIALIZABLE);

                // broadcast null
                if (broadcast == null) {
                    // log
                    L.e(mtn + "broadcast[" + broadcast + "] is not ready.");

                    // exit from this process
                    return;

                }

                // broadcast conditions
                broadcastConditions(broadcast);

            } catch (Exception e) {
                e.printStackTrace();
                L.e(mtn + "Err: " + e.getMessage());
            }
        }
    };

    // explicit variables
    private final double FIXED_ACCURACY = 700;
    private final double FIXED_GPS_ACQUIRING = 1;
    private boolean GPS_ACQUIRED = false;
    private int acquiring_count = 0;

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
                // update notify content
                updateNotifyContent();

                // prepare usage variables
                Intent i = new Intent();
                BroadcastObject broadcastObject = new BroadcastObject();
                RecorderObject record = new RecorderObject();

                //--> record props
                record.displayRecordAsTime = recorderUtils.displayRecordAsTime;
                record.displayPaceAsTime = recorderUtils.displayPaceAsTime;
                record.paceMillis = recorderUtils.paceMillis;
                record.durationMillis = recorderUtils.recordDurationMillis;
                record.distanceKm = recorderUtils.distanceKm;
                record.calories = recorderUtils.calories;
                record.xLoc = (lastLocation != null)
                        ? new xLocation(lastLocation.latitude, lastLocation.longitude)
                        : null;
                record.gpsAcquired = GPS_ACQUIRED;

                //--> broadcast props
                broadcastObject.attachedObject = (currentRecorder = record);
                broadcastObject.broadcastType = BroadcastType.RECORDING;

                //--> intent props
                i.setAction(Globals.BROADCAST_TEST);
                i.putExtra(Globals.SERIALIZABLE, broadcastObject);

                // send broadcast
                sendBroadcast(i);

            }
        });
        recorderUtils.start();

        // update flag
        onRecordStarted = true;

        // initial location manager
        initialLocationManager();

//
//        // update props
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            // prepare usage variables
//            final String mtn = ct + "getLastLocation() ";
//
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null && location.getAccuracy() <= FIXED_ACCURACY) {
//                    xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude());
//
////                    broadcast( Globals.GSON.toJson( xLoc ) );
////
////                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
//                }
//            }
//        });
//
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(1);
//        mLocationRequest.setInterval(1 * 1000);
//        mLocationRequest.setFastestInterval(1 * 1000);
//        mLocationCallback = new LocationCallback() {
//            // prepare usage variables
//            final String mtn = ct + "LocationCallback() - test ";
//
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                // location is not ready
//                if (onRecordPaused || locationResult == null) return;
//
//                // loop locations
//                for (Location location : locationResult.getLocations()) {
//                    // useless location
//                    if (location == null || location.getAccuracy() > (FIXED_ACCURACY)) return;
//
//                    // acquiring
//                    final boolean gpsAcquiring = acquiring_count < FIXED_GPS_ACQUIRING;
//                    // gps acquiring condition
//                    if (gpsAcquiring) ++acquiring_count;
//                    else if (!gpsAcquiring && !GPS_ACQUIRED) {
//                        // update flag
//                        GPS_ACQUIRED = true;
//
//                    }
//
//                    // does gps acquired
//                    if (!GPS_ACQUIRED) {
//                        // logs
//                        L.i(mtn + "gps acquiring amount[" + acquiring_count + "/" + FIXED_GPS_ACQUIRING + "]");
//
//                        // exit from this process
//                        return;
//                    }
//
//                    // accepted location
//                    xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude()
//                            , location.getAccuracy());
//
//                    // broadcast
////                    broadcast(Globals.GSON.toJson(xLoc));
//
//                    // last location condition
//                    if (lastLocation == null) {
//                        // log
//                        L.i(mtn + "update last location.");
//
//                        // update last location;
//                        lastLocation = xLoc;
//
//                        // keep point
//                        points.add(new LatLng(xLoc.latitude, xLoc.longitude));
//
//                        // exit from this process
//                        return;
//                    }
//
//                    // difference distance
//                    final double differenceDist = recorderUtils.differenceDistance(xLoc, lastLocation);
//
//                    // should add distance
//                    if (differenceDist > 0.005) {
//                        // keep point
//                        points.add(new LatLng(xLoc.latitude, xLoc.longitude));
//
//                        // update distance
//                        recorderUtils.addDistance(differenceDist);
//
//                        // broadcast location
//                        // prepare usage variables
//                        Intent i = new Intent();
//                        RecorderObject record = new RecorderObject();
//                        BroadcastObject broadcastObject = new BroadcastObject();
//
//                        //--> record props
//                        record.xLocCurrent = new xLocation(xLoc.latitude, xLoc.longitude);
//                        record.xLocLast = lastLocation;
//                        record.displayRecordAsTime = recorderUtils.displayRecordAsTime;
//                        record.displayPaceAsTime = recorderUtils.displayPaceAsTime;
//                        record.durationMillis = recorderUtils.recordDurationMillis;
//                        record.paceMillis = recorderUtils.paceMillis;
//                        record.distanceKm = recorderUtils.distanceKm;
//                        record.calories = recorderUtils.calories;
//                        record.gpsAcquired = GPS_ACQUIRED;
//
//                        //--> broadcast attached object
//                        broadcastObject.broadcastType = BroadcastType.LOCATION;
//                        broadcastObject.attachedObject = record;
//
//                        //--> intent props
//                        i.setAction(Globals.BROADCAST_TEST);
//                        i.putExtra(Globals.SERIALIZABLE, broadcastObject);
//                        // send broadcast
//                        sendBroadcast(i);
//
//                        // update last location
//                        lastLocation = xLoc;
//
////                        L.i(mtn + "Location accepted..");
//                    }
//
////                    L.i(mtn + "");
////                    L.i(mtn + "");
////                    L.i(mtn + " * * * Location Information * * * ");
////                    L.i(mtn + "--> timing");
////                    L.i(mtn + "Pace: " + recorderUtils.displayPaceAsTime);
////                    L.i(mtn + "Record duration: " + recorderUtils.displayRecordAsTime);
////                    L.i(mtn + "--> Unit");
////                    L.i(mtn + "Calorie: " + recorderUtils.calories);
////                    L.i(mtn + "Distance: " + recorderUtils.distanceKm);
////                    L.i(mtn + "--> Ect.");
////                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
////                    L.i(mtn + "");
////                    L.i(mtn + "");
////                    L.i(mtn + "");
//
//                }
//            }
//        };
//        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
//
        // display foreground
        startForeground();

        // alert / check gps acquiring
        alertGPSAcquiring(true);

    }


    /**
     * Feature methods
     */
    private void updateNotifyContent(){
        // prepare usage variables
        final String mtn = ct +"updateNotifyContent() ";

        if (notificationBuilder != null) {
            try {
                // prepare usage variables
                String displayTime = recorderUtils.displayRecordAsTime;
                String stringContent = stringPattern(displayTime, Globals.DCM_2.format(recorderUtils.distanceKm));

                stringContent += " accuracy: "+ ((lastLocation != null )
                        ? lastLocation.accuracy +"" : "0");

                // update props
                notificationBuilder.setContentText(stringContent );

                // update notify
                notificationManager.notify(NOTIF_ID, notificationBuilder.build());

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());

            }

        }
    }
    private void initialLocationManager() {
        // prepare usage variables
        final String mtn = ct + "initialLocationManager() ";
        L.i(mtn + "called...");

        // condition
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            L.i(mtn + "need location permission.");
            return;
        }
        ;
        // prepare usage variables
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // broadcast location
        // prepare usage variables
        Intent i = new Intent();
        DebugUIObject debug = new DebugUIObject();
        BroadcastObject broadcastObject = new BroadcastObject();

        //--> debug
        debug.xLocation = new xLocation(0.0, 0.0, 0);
        debug.isGPSEnabled = isGPSEnabled;
        debug.isNetworkEnabled = isNetworkEnabled;
        //--> broadcast
        broadcastObject.broadcastType = BroadcastType.ACTIONS;
        broadcastObject.broadcastAction = BroadcastAction.UI_DEBUG_UPDATE;
        broadcastObject.attachedObject = debug;

        //--> intent props
        i.setAction(Globals.BROADCAST_TEST);
        i.putExtra(Globals.SERIALIZABLE, broadcastObject);

        // send broadcast
        sendBroadcast(i);

        L.i(mtn + "isGPSEnabled: " + isGPSEnabled);
        L.i(mtn + "isNetworkEnabled: " + isNetworkEnabled);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1 * 1000,
                1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // prepare usage variables
                        final String msg = "location-changed: " + location.getLatitude() + " " + location.getLongitude();

                        L.i(mtn + msg);
//                        Toast.makeText(BackgroundService.this, msg, Toast.LENGTH_SHORT).show();

                        // update flag
                        GPS_ACQUIRED = true;

                        // broadcast location
                        // prepare usage variables
                        Intent i = new Intent();
                        DebugUIObject debug = new DebugUIObject();
                        BroadcastObject broadcastObject = new BroadcastObject();

                        //--> debug
                        debug.xLocation = new xLocation(location.getLatitude(), location.getLongitude()
                                , location.getAccuracy());
                        debug.isGPSEnabled = isGPSEnabled;
                        debug.isNetworkEnabled = isNetworkEnabled;
                        //--> broadcast
                        broadcastObject.broadcastType = BroadcastType.ACTIONS;
                        broadcastObject.broadcastAction = BroadcastAction.UI_DEBUG_UPDATE;
                        broadcastObject.attachedObject = debug;

                        //--> intent props
                        i.setAction(Globals.BROADCAST_TEST);
                        i.putExtra(Globals.SERIALIZABLE, broadcastObject);

                        // send broadcast
                        sendBroadcast(i);

                        // received location
                        receivedLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

    }

    private void notifyController(boolean forceAction) {
        final String mtn = ct + "notifyController() ";

        // notification builder is null
        if (notificationBuilder == null) {
            // exit from this process
            return;
        }

        try {
            NotificationCompat.Action actionButton = notificationBuilder.mActions.get(0);

            // job must started
            if (onRecordStarted) {
                // update flag
                onRecordPaused = forceAction ? ((onRecordPaused) ? false : true)
                        : onRecordPaused;

                // record state conditions
                if (onRecordPaused) {
                    L.i(mtn + "going to pause");
                    // resume recording
                    if (forceAction) pauseRecording();

                    // update button description
                    actionButton.title = "RESUME";

                } else {
                    L.i(mtn + "going to resume");

                    // resume recording
                    if (forceAction) resumeRecording();

                    // update button description
                    actionButton.title = "STOP";
                }

                // notify to
                notificationManager.notify(NOTIF_ID, notificationBuilder.build());

            } else L.e(mtn + "record[" + onRecordStarted + "] does not start yet.");

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

    }

    private void receivedLocation(Location location) {
        // prepare usage variables
        final String mtn = ct + "receivedLocation() ";
        // location is not ready
        if (onRecordPaused || location == null) return;

        // loop locations
        // useless location
        if (location == null || location.getAccuracy() > (FIXED_ACCURACY)) return;

        // acquiring
        final boolean gpsAcquiring = acquiring_count < FIXED_GPS_ACQUIRING;
        // gps acquiring condition
        if (gpsAcquiring) ++acquiring_count;
        else if (!gpsAcquiring && !GPS_ACQUIRED) {
            // update flag
            GPS_ACQUIRED = true;

        }

        // does gps acquired
        if (!GPS_ACQUIRED) {
            // logs
            L.i(mtn + "gps acquiring amount[" + acquiring_count + "/" + FIXED_GPS_ACQUIRING + "]");

            // exit from this process
            return;
        }

        // accepted location
        xLocation xLoc = new xLocation(location.getLatitude(), location.getLongitude()
                , location.getAccuracy());

        // broadcast
//                    broadcast(Globals.GSON.toJson(xLoc));

        // last location condition
        if (lastLocation == null) {
            // log
            L.i(mtn + "update last location.");

            // update last location;
            lastLocation = xLoc;

            // keep point
            points.add(new LatLng(xLoc.latitude, xLoc.longitude));

            // exit from this process
            return;
        }

        // difference distance
        final double differenceDist = recorderUtils.differenceDistance(xLoc, lastLocation);

        // should add distance
        if (differenceDist > 0.005) {
            // keep point
            points.add(new LatLng(xLoc.latitude, xLoc.longitude));

            // update distance
            recorderUtils.addDistance(differenceDist);

            // broadcast location
            // prepare usage variables
            Intent i = new Intent();
            RecorderObject record = new RecorderObject();
            BroadcastObject broadcastObject = new BroadcastObject();

            //--> record props
            record.xLocCurrent = new xLocation(xLoc.latitude, xLoc.longitude, xLoc.accuracy);
            record.xLocLast = lastLocation;
            record.displayRecordAsTime = recorderUtils.displayRecordAsTime;
            record.displayPaceAsTime = recorderUtils.displayPaceAsTime;
            record.durationMillis = recorderUtils.recordDurationMillis;
            record.paceMillis = recorderUtils.paceMillis;
            record.distanceKm = recorderUtils.distanceKm;
            record.calories = recorderUtils.calories;
            record.gpsAcquired = GPS_ACQUIRED;

            //--> broadcast attached object
            broadcastObject.broadcastType = BroadcastType.LOCATION;
            broadcastObject.attachedObject = record;

            //--> intent props
            i.setAction(Globals.BROADCAST_TEST);
            i.putExtra(Globals.SERIALIZABLE, broadcastObject);
            // send broadcast
            sendBroadcast(i);

            // update last location
            lastLocation = xLoc;

//                        L.i(mtn + "Location accepted..");
        }

//                    L.i(mtn + "");
//                    L.i(mtn + "");
//                    L.i(mtn + " * * * Location Information * * * ");
//                    L.i(mtn + "--> timing");
//                    L.i(mtn + "Pace: " + recorderUtils.displayPaceAsTime);
//                    L.i(mtn + "Record duration: " + recorderUtils.displayRecordAsTime);
//                    L.i(mtn + "--> Unit");
//                    L.i(mtn + "Calorie: " + recorderUtils.calories);
//                    L.i(mtn + "Distance: " + recorderUtils.distanceKm);
//                    L.i(mtn + "--> Ect.");
//                    L.i(mtn + "xLoc: " + xLoc.latitude + "," + xLoc.longitude);
//                    L.i(mtn + "");
//                    L.i(mtn + "");
//                    L.i(mtn + "");

    }

    private void alertGPSAcquiring(boolean recursive) {

        // prepare usage variables
        final String mtn = ct + "alertGPSAcquiring() ";
        Handler handler = new Handler();
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                // prepare usage variables
                Intent i = new Intent();
                BroadcastObject broadcastObject = new BroadcastObject();
                RecorderObject recorder = new RecorderObject();
                boolean _gpsAcquiring = GPS_ACQUIRED;

                //--> update props
                recorder.gpsAcquired = _gpsAcquiring;

                //--> broadcast
                broadcastObject.broadcastType = BroadcastType.ACTIONS;
                broadcastObject.broadcastAction = BroadcastAction.GPS_ACQUIRING;
                broadcastObject.attachedObject = recorder;

                //--> intent props
                i.setAction(Globals.BROADCAST_TEST);
                i.putExtra(Globals.SERIALIZABLE, broadcastObject);

                // send broadcast
                sendBroadcast(i);

                // exit from recursive
                if (_gpsAcquiring || !recursive) {
                    // logs
                    L.i(mtn + "exit from \"alert gps acquiring\" ");
                    // exit from this process
                    return;
                }

                // recursive
                if (!GPS_ACQUIRED) alertGPSAcquiring(true);
                else {
                    // recursive
                    if (GPS_ACQUIRED && recursive) alertGPSAcquiring(false);

                }

            }
        };

        handler.postDelayed(runner, 1000);


    }

    private void resumeRecording() {
        // prepare usage variables
        final String mtn = ct + "resumeRecording() ";

        if (recorderUtils != null) {
            // update flag
            onRecordPaused = false;

            // pause recorder
            recorderUtils.start();

        } else L.e(mtn + "recorder[" + recorderUtils + "] is not ready.");
    }

    private void pauseRecording() {
        // prepare usage variables
        final String mtn = ct + "pauseRecording() ";

        if (recorderUtils != null) {
            // update flag
            onRecordPaused = true;

            // pause recorder
            recorderUtils.pause();

        } else L.e(mtn + "recorder[" + recorderUtils + "] is not ready.");
    }

    private void broadcastConditions(BroadcastObject broadcast) {
        // prepare usage variables
        final String mtn = ct + "broadcastConditions() ";
        final String actionType = broadcast.broadcastAction.TYPE;

        L.i(mtn + "Action type: " + actionType);

        // conditions
        if (actionType.equalsIgnoreCase(BroadcastAction.INITIAL.TYPE)) {

            // prepare usage variables
            // create points instance
            Globals.POINTS = new ArrayList<>();

            //--> flush all points
            Globals.POINTS.addAll(points);

            // prepare usage variables
            Intent i = new Intent();
            BroadcastObject broadcastObject = new BroadcastObject();

            //--> update props
            broadcastObject.broadcastType = BroadcastType.ACTIONS;
            broadcastObject.broadcastAction = BroadcastAction.INITIAL;

            //--> intent props
            i.setAction(Globals.BROADCAST_TEST);
            i.putExtra(Globals.SERIALIZABLE, broadcastObject);

            // send broadcast
            sendBroadcast(i);

        } else if (actionType.equalsIgnoreCase(BroadcastAction.PAUSE.TYPE)) {
            // pause recording
            pauseRecording();

            // notify controller
            notifyController(false);

        } else if (actionType.equalsIgnoreCase(BroadcastAction.RESUME.TYPE)) {
            // resume recording
            resumeRecording();

            // notify controller
            notifyController(false);

        } else if (actionType.equalsIgnoreCase(BroadcastAction.RESET.TYPE)) {

            if (recorderUtils != null) {
                // stop and reset all
                // variables and prepare for next start
                onRecordPaused = true;

                // clear props
                points.clear();
                //--> last location
                lastLocation = null;
                //--> current record
                currentRecorder = null;

                // reset recorder
                recorderUtils.reset();

            } else L.e(mtn + "recorder[" + recorderUtils + "] is not ready.");

        } else if (actionType.equalsIgnoreCase(BroadcastAction.GET_BACKGROUND_SERVICE_INFO.TYPE)) {
            // prepare usage variables
            Intent i = new Intent();
            BroadcastObject broadcastObject = new BroadcastObject();

            //--> update props
            broadcastObject.broadcastType = BroadcastType.ACTIONS;
            broadcastObject.broadcastAction = BroadcastAction.GET_BACKGROUND_SERVICE_INFO;
            broadcastObject.attachedObject = serviceInfoObject(currentRecorder);

            //--> intent props
            i.setAction(Globals.BROADCAST_TEST);
            i.putExtra(Globals.SERIALIZABLE, broadcastObject);

            // send broadcast
            sendBroadcast(i);

        } else if (actionType.equalsIgnoreCase(BroadcastAction.NONE.TYPE)) {
            // notify controller
            notifyController(true);

            // prepare usage variables
            Intent i = new Intent();
            BroadcastObject broadcastObject = new BroadcastObject();

            //--> record props
            //--> broadcast props
            broadcastObject.broadcastType = BroadcastType.ACTIONS;
            broadcastObject.broadcastAction = BroadcastAction.UI_UPDATE;
            broadcastObject.attachedObject = serviceInfoObject(currentRecorder);

            //--> intent props
            i.setAction(Globals.BROADCAST_TEST);
            i.putExtra(Globals.SERIALIZABLE, broadcastObject);

            // send broadcast
            sendBroadcast(i);

        } else L.e(mtn + "Action type[" + actionType + "] does not matches.");
    }

    private void unregisterBroadcast() {
        unregisterReceiver(broadcastReceiver);
    }

    private void registerBroadcast() {
        registerReceiver(broadcastReceiver, new IntentFilter(Globals.BROADCAST_SERVICE));

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
     * Getter
     */
    private PendingIntent intentBroadcastReceiverService() {
        // prepare usage variables
        Intent i = new Intent();
        BroadcastObject broadcastObject = new BroadcastObject();

        //--> update props
        broadcastObject.broadcastType = BroadcastType.ACTIONS;
        broadcastObject.broadcastAction = BroadcastAction.NONE;

        // update intent
        i.setAction(Globals.BROADCAST_SERVICE);
        i.putExtra(Globals.SERIALIZABLE, broadcastObject);

        // prepare pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

        return pendingIntent;
    }

    private BackgroundServiceInfoObject serviceInfoObject(RecorderObject recorder) {
        // prepare usage variables
        BackgroundServiceInfoObject info = new BackgroundServiceInfoObject();

        // update props
        info.isRecordPaused = onRecordPaused;
        info.isRecordStarted = onRecordStarted;
        info.attachedObject = recorder;

        return info;
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

        // if(
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
    private NotificationCompat.InboxStyle inboxStyle;
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    private void startForeground() {
        // prepare usage variables
        Intent notificationIntent = new Intent(this, BridgeFile.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create notification channel
        createNotificationChannel();

        // create inbox style
        Spannable sb = new SpannableString(stringPattern("00:00:00", "0.0"));
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_lay);
        Notification notification = (notificationBuilder = new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                //--> display
                .setSmallIcon(R.drawable.ic_logo_small)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                //--> actions
                .addAction(R.drawable.ic_record, "STOP", intentBroadcastReceiverService())
                //--> custom
//                .setShowWhen(false)
                .setOnlyAlertOnce(true)
//                .setStyle(((inboxStyle == null) ? inboxStyle = new NotificationCompat.InboxStyle()
////                        .addLine("line-1")
////                        .addLine("line-2")
////                        .addLine(sb)
////                        .setBigContentTitle("Big Content Title")
//                        .setSummaryText("Summary Text")
//                        : inboxStyle))
                .setContentText(sb)
//                .setContentText("Background service is running.")

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


    /**
     * Micro methods
     */
    private String stringPattern(String displayTime, String displayKm) {
        return String.format(getString(R.string.notification_display_info),
                getString(R.string.u250),
                displayTime,
                getString(R.string.u250),
                displayKm);

    }
}
