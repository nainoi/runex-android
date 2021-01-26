package com.think.runex.java.Pages.RecordPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.UiSettings;
import com.google.android.libraries.maps.model.LatLng;
import com.think.runex.R;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.BroadcastAction;
import com.think.runex.java.Constants.BroadcastType;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Fragment.xFragmentHandler;
import com.think.runex.java.Customize.Views.xMapViewGroup;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Models.WorkoutObject;
import com.think.runex.java.Models.BackgroundServiceInfoObject;
import com.think.runex.java.Models.BroadcastObject;
import com.think.runex.java.Models.DebugUIObject;
import com.think.runex.java.Models.EventIdAndPartnerObject;
import com.think.runex.java.Models.RealmPointObject;
import com.think.runex.java.Models.RealmRecorderObject;
import com.think.runex.java.Pages.ReviewEvent.ActiveRegisteredEventCheckerPage;
import com.think.runex.java.Pages.ReviewEvent.OnConfirmEventsListener;
import com.think.runex.java.Pages.SharePage;
import com.think.runex.java.Pages.SuccessfullySubmitRunningResultPage;
import com.think.runex.java.Services.BackgroundService;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.Animation.AnimUtils;
import com.think.runex.java.Utils.Animation.onAnimCallback;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.GPSFileRecorder;
import com.think.runex.java.Utils.GoogleMap.GoogleMapUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.Network.Request.rqAddWorkOutsHistory;
import com.think.runex.java.Utils.Network.Request.rqSubmitActivitiesWorkout;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.AddWorkOutsService;
import com.think.runex.java.Utils.Network.Services.SubmitActivitiesWorkoutService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.PermissionUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.think.runex.java.Constants.Globals.RC_PERMISSION_SAVE_FILE;

public class RecordPage extends xFragment implements OnMapReadyCallback
        , View.OnClickListener
        , OnConfirmEventsListener {
    /**
     * Main variables
     */
    private final String ct = "RecordPage->";

    // instance variables
    private ActiveRegisteredEventCheckerPage mActiveRegisteredEventCheckerPageDialog;
    // instance variables
    private FragmentUtils mFragmentUtils;
    private LocationUtils mLocUtils;
    private PermissionUtils mPmUtils;
    private GoogleMapUtils mMapUtils;
    private GoogleMap mMap;
    private xLocation mLastLocation;
    private RealmRecorderObject currentRecorder;
    private Realm realm;
    private WorkoutInfo workoutInfo;
    private List<EventIdAndPartnerObject> selectedEvents;

    private BroadcastReceiver testBroadcastReceiver = new BroadcastReceiver() {
        // prepare usage variables
        final String mtn = ct + "testBroadcastReceiver() ";

        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                // prepare usage variables
                BroadcastObject broadcastObj = intent.getParcelableExtra(Globals.SERIALIZABLE);
                if (broadcastObj == null) {
                    return;
                }
                try {
                    // prepare usage variables
                    BroadcastType broadcastType = broadcastObj.broadcastType;

                    // conditions
                    if (broadcastType.equals(BroadcastType.RECORDING) && isMapReady()) {
                        // prepare usage variables
                        final RealmRecorderObject recorder = broadcastObj.recorderObject;

                        // update props
                        currentRecorder = recorder;

                        // binding views
                        binding(recorder);

                        // update map camera
                        zoomOnce(recorder);

                    } else if (broadcastType.equals(BroadcastType.LOCATION) && isMapReady()) {
                        // prepare usage variables
                        final RealmRecorderObject recorder = broadcastObj.recorderObject;

                        // tracking
                        mMapUtils.tracking(recorder.xLocCurrent);

                        // add polyline
                        mMapUtils.addPolyline(recorder.xLocLast, recorder.xLocCurrent);

                    } else if (broadcastType.equals(BroadcastType.ACTIONS)) {
                        // prepare usage variables
                        final BroadcastAction action = broadcastObj.broadcastAction;
                        L.i(mtn + "broadcast action: " + action);

                        if (action.equals(BroadcastAction.INITIAL) && isMapReady()) {
                            // update all points
                            mMapUtils.points.addAll(getAllPoint());

                            // prepare usage variables
                            final List<LatLng> points = mMapUtils.points;

                            if (points.size() > 1) {
                                // prepare usage variables
                                final LatLng lastPoint = points.remove(points.size() - 1);
                                final LatLng fromPoint = points.get(points.size() - 1);
                                final xLocation from = new xLocation(fromPoint.latitude, fromPoint.longitude);
                                final xLocation to = new xLocation(lastPoint.latitude, lastPoint.longitude);

                                // redraw
                                mMapUtils.addPolyline(from, to);

                            }

                        } else if (action.equals(BroadcastAction.GPS_POOR_SIGNAL)) {
                            // prepare usage variables
                            final RealmRecorderObject record = broadcastObj.recorderObject;

                            // gps conditions
                            if (record.gpsPoorSignal) onPoorGPSSignal();
                            else onGPSAcquired();

                        } else if (action.equals(BroadcastAction.GPS_ACQUIRING)) {
                            // prepare usage variables
                            final RealmRecorderObject record = broadcastObj.recorderObject;

                            // gps conditions
                            if (record.gpsAcquired) onGPSAcquired();
                            else onGPSSignalLost();

//                            L.i(mtn + "is gps acquiring: " + record.gpsAcquired);

                        } else if (action.equals(BroadcastAction.UI_UPDATE)) {
                            // prepare usage variables
                            final BackgroundServiceInfoObject info = broadcastObj.serviceInfoObject;
                            final RealmRecorderObject record = info.recorderObject;

                            // record is not ready
                            if (record == null) {
                                // log
                                L.i(mtn + "record[" + record + "] is not ready.");

                                // exit from this process
                                return;
                            }

                            // state condition
                            if (info.isRecordPaused) {
                                L.i(mtn + "display paused state buttons.");
                                // update flag
                                mOnRecording = false;

                                // anim pause
                                anim(R.drawable.ic_play);

                                // display on paused
                                displayOnPaused(record);

                            } else if (info.isRecordStarted && !info.isRecordPaused) {
                                L.i(mtn + "display recording state buttons.");

                                // update flag
                                mOnRecording = true;

                                // anim pause
                                anim(R.drawable.ic_pause);

                                // display on started
                                displayOnStarted(record);

                            }

                        } else if (action.equals(BroadcastAction.GET_BACKGROUND_SERVICE_INFO)) {
                            // prepare usage variables
                            final BackgroundServiceInfoObject info = broadcastObj.serviceInfoObject;
                            final RealmRecorderObject record = (currentRecorder = info.recorderObject);

                            L.i(mtn + " * * * background-service info * * * ");
                            L.i(mtn + "record > is pause: " + info.isRecordPaused);

                            if (info.isRecordPaused && record != null) {
                                L.i(mtn + "display paused state buttons.");

                                // display on paused
                                displayOnPaused(record);

                            } else if (info.isRecordStarted && !info.isRecordPaused) {
                                L.i(mtn + "display recording state buttons.");

                                // display on started
                                displayOnStarted(record);

                                // start / continue record activity
                                start();
                            }

                            L.i(mtn + " ");

                        } else if (action.equals(BroadcastAction.UI_DEBUG_UPDATE)) {
                            // prepare usage variables
                            final DebugUIObject debug = broadcastObj.debugUIObject;

                            L.i(mtn + " * * * debug * * * ");
                            L.i(mtn + "debug-object: " + Globals.GSON.toJson(debug));

                            View d = getView().findViewById(R.id.frame_debug);
                            d.setVisibility(View.VISIBLE);
                            TextView lbGPSEnabled = d.findViewById(R.id.lb_gps_enabled);
                            TextView lbNetworkEnabled = d.findViewById(R.id.lb_network_enabled);
                            TextView lbAccuracy = d.findViewById(R.id.lb_location_accuracy);
                            TextView lbAvgAccuracy = d.findViewById(R.id.lb_location_avg_accuracy);
                            TextView lbLat = d.findViewById(R.id.lb_location_latitude);
                            TextView lbLon = d.findViewById(R.id.lb_location_longitude);

                            lbGPSEnabled.setText((debug.isGPSEnabled + "").toUpperCase() + "");
                            lbNetworkEnabled.setText((debug.isNetworkEnabled + "").toUpperCase() + "");
                            lbAccuracy.setText("Accuracy: " + (debug.xLocation.accuracy + "").toUpperCase() + " Meters");
                            lbAvgAccuracy.setText("Avg-Accuracy: " + (debug.xLocation.avgAccuracy + "").toUpperCase() + " Meters");
                            lbLat.setText("Latitude: " + (debug.xLocation.latitude + "").toUpperCase() + "");
                            lbLon.setText("Longitude: " + (debug.xLocation.longitude + "").toUpperCase() + "");

                        } else L.e(mtn + "Broadcast action[" + action + "] does not matches.");

                    } else
                        L.e(mtn + "Broadcast type[" + broadcastType + "] does not matches, Or map status[" + isMapReady() + "]");

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

            } catch (Exception e) {
                e.printStackTrace();
                L.e(mtn + "Err: " + e.getMessage());
            }
        }
    };
    //--> GPS record file
    private GPSFileRecorder gpsFileRecorder;

    // explicit variables
    private boolean onNetwork = false;
    private boolean mZoomOnce = false;
    private boolean mOnRecording = false;
    private boolean mOnSubmitMultiResult = false;
    private boolean mInitMap = false;
    private int CONTAINER_ID = R.id.display_fragment_frame;
    private xFragment pageSuccessfully;
    private boolean mOnDisplaySummary = false;
    //--> XRequest code
    private final int XRC_REMOVE_ALL_FRAGMENT = 1001;

    // views
    private View mView;
    private TextView lbRecordingState;
    private AppCompatImageView icRecordState;
    private TextView lbTime;
    private TextView lbDistance;
    private TextView lbCalories;
    private TextView lbPace;
    private FrameLayout frameDistanceInMap;
    private TextView distanceInMapLabel, durationInMapLabel, dateTimeLabelInMap;
    private ImageView runexLogo;
    //-->Labs
    private TextView labAccuracy;
    //--> Frame map
    private xMapViewGroup frameMap;
    //--> Frame share
    private View frameToolbar;
    private View btnSocialShare;
    //--> Frame recording
    private View frameRecording;

    private TextView btnStart;
    //private TextView btnSaveWithoutSubmitResult;
    private View btnStopAndSubmit;
    private ImageView previewImage;
    //--> Toolbar
    private View btnCancelSubmit;
    //--> Change background image
    //private View frameChangeBgImage;
    //--> Running summary frame
    private View frameSummary;
    private TextView inputDistance, inputDisplayTime, inputPaceDisplayTime, inputCalories;
    private View btnSubmit;

    public void onBackPressed() {

        if (mOnDisplaySummary) {
            // prepare usage variables
            final DialogInterface.OnClickListener listener = getConfirmationDialogListener();

            // confirmation dialog
            dialogDiscardRecording("ยืนยัน",
                    "คุณต้องการออกจากหน้านี้โดยไม่บันทึกผลการวิ่ง ?",
                    listener).show();


        }
    }


    /**
     * Implement methods
     */
    @Override
    public void onClick(View view) {
        final String mtn = ct + "onClick() ";

        switch (view.getId()) {
            case R.id.btn_cancel_submit:
                onBackPressed();
                break;

            case R.id.btn_share:
                // share
                share();

                break;
//            case R.id.btn_save_without_submit_result:
//                if (onNetwork) return;
//
//                // update flag
//                onNetwork = true;
//
//                // save record without submit
//                apiSaveRecord(null);
//
//                break;

            case R.id.btn_submit_result:

                // condition
                if (onNetwork) return;

                // save record with submit
                toReviewEventPage();

                break;

            case R.id.frame_change_background_image:
                // open image gallery picker
                DeviceUtils.instance(activity).openImagePicker(activity, Globals.RC_PICK_IMAGE);

                break;

            case R.id.btn_start:
                if (mMap == null) {
                    // does gps available
                    mLocUtils.isGPSAvailable();

                    // exit from this process
                    return;
                }

                // init gps file recorder
                if (gpsFileRecorder == null)
                    gpsFileRecorder = new GPSFileRecorder("gps.txt", activity);

                // going to stop recording
                if (mOnRecording) {
                    // log
                    L.i(mtn + "on recording. -> going to pause recording.");

                    // paused
                    paused();

                    // exit from activity process
                    return;

                }

                // start
                start();

                break;

            case R.id.frame_stop_and_submit:
                // paused
                paused();

                // display confirm stop recording
                dialogDiscardRecording("ยืนยัน", "คุณยืนยันที่จะสิ้นสุดการวิ่ง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // conditions
                        if (DialogInterface.BUTTON_POSITIVE == i) {

                            // distance condition
                            if (currentRecorder != null && currentRecorder.distanceKm > 0) {
                                // zoom to fit
                                mMapUtils.zoomToFit();

                                // display summary frame
                                displaySummaryFrame(currentRecorder);

                                // keep temporary recorder
                                keepTemporaryRecorder();

                                // reset service
                                resetService();

                                // stop service
                                stopService();

                                // binding views
                                binding(new RealmRecorderObject());

                                apiWorkOuts(null);

                            } else {
                                // reset service
                                resetService();

                                // stop service
                                stopService();

                                // binding views
                                binding(new RealmRecorderObject());

                                // to begin
                                toBegin(true);
                            }

                            // dismiss
                        } else dialogInterface.dismiss();

                    }
                }).show();

                break;
        }
    }

    /**
     * Service methods
     */
    public void resetService() {
        // prepare usage variables
        final Intent i = new Intent();
        final BroadcastObject broadcast = new BroadcastObject();

        //--> update props
        broadcast.broadcastType = BroadcastType.ACTIONS;
        broadcast.broadcastAction = BroadcastAction.RESET;
        //-- intent props
        i.setAction(Globals.BROADCAST_SERVICE);
        i.putExtra(Globals.SERIALIZABLE, broadcast);

        // broadcast
        activity.sendBroadcast(i);
    }

    public void resumeService() {
        // prepare usage variables
        final Intent i = new Intent();
        final BroadcastObject broadcast = new BroadcastObject();

        //--> update props
        broadcast.broadcastType = BroadcastType.ACTIONS;
        broadcast.broadcastAction = BroadcastAction.RESUME;
        //-- intent props
        i.setAction(Globals.BROADCAST_SERVICE);
        i.putExtra(Globals.SERIALIZABLE, broadcast);

        // broadcast
        activity.sendBroadcast(i);
    }

    public void pauseService() {
        // prepare usage variables
        final Intent i = new Intent();
        final BroadcastObject broadcast = new BroadcastObject();

        //--> update props
        broadcast.broadcastType = BroadcastType.ACTIONS;
        broadcast.broadcastAction = BroadcastAction.PAUSE;
        //-- intent props
        i.setAction(Globals.BROADCAST_SERVICE);
        i.putExtra(Globals.SERIALIZABLE, broadcast);

        // broadcast
        activity.sendBroadcast(i);
    }

    public void startService() {
        // prepare usage variables
        final String mtn = ct + "startService() ";

        if (isMyServiceRunning(BackgroundService.class)) {
            L.i(mtn + "Service is running, Going to resume...");

            // exit from this process
            return;

        } else L.i(mtn + "Start service.");

        // use activity to start and trigger a service
        Intent i = new Intent(activity, BackgroundService.class);
        // potentially add data to the intent
        i.putExtra("KEY1", "Value to be used by the service");

        // start service
        activity.startService(i);
    }

    // Stop the service
    public void stopService() {
        activity.stopService(new Intent(activity, BackgroundService.class));

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // prepare usage variables
        final String mtn = ct + "onMapRead() ";

        // update props
        mMap = googleMap;

        //--> Map utils
        mMapUtils = GoogleMapUtils.newInstance(mMap);
        //--> should redraw
        shouldRedrawPolyline();

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);

        mMap.setMyLocationEnabled(true);

        // prepare usage variables
        Location lkLoc = mLocUtils.getLastKnownLocation();

        // last known not found
        if (lkLoc != null) {
            // prepare usage variables
            LatLng ll = new LatLng(lkLoc.getLatitude(), lkLoc.getLongitude());

            // update map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));

        } else L.e(mtn + "last know location does not exists: " + lkLoc);

        // broadcast props
        BroadcastObject broadcast = new BroadcastObject();
        //--> update props
        broadcast.broadcastType = BroadcastType.ACTIONS;
        broadcast.broadcastAction = BroadcastAction.INITIAL;

        // broadcast service
        broadcastService(broadcast);

//        xLocation x1 = new xLocation(13.845689, 100.596905);
//
//        xLocation x11 = new xLocation(13.844689, 100.596905);
//        xLocation x111 = new xLocation(13.843689, 100.596905);
//        xLocation x1111 = new xLocation(13.840689, 100.596905);
//        xLocation x11111 = new xLocation(13.835689, 100.580905);

//        xLocation x2 = new xLocation(13.845585, 100.594587);
//        xLocation x3 = new xLocation(13.842551, 100.595622);
//        xLocation x4 = new xLocation(13.843457, 100.597479);
//        xLocation x5 = new xLocation(13.842600, 100.598379);
////
//        locationChanged(x1);
////
//        locationChanged(x11);
//        locationChanged(x111);
//        locationChanged(x1111);
//        locationChanged(x11111);

//        locationChanged(x2);
//        locationChanged(x3);
//        locationChanged(x4);
//        locationChanged(x5);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        mView = inflater.inflate(R.layout.page_record, container, false);

        CONTAINER_ID = R.id.display_fragment_frame;
        // Fragment inits
        mFragmentUtils = FragmentUtils.newInstance(requireActivity(), CONTAINER_ID);

        // views matching
        viewMatching(mView);

        // view event listener
        viewEventListener();

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance(activity);
        actUtls.fullScreen();

        // buttons state
        if (isMyServiceRunning(BackgroundService.class)) {
            // prepare usage variables
            final Intent i = new Intent();
            final BroadcastObject broadcast = new BroadcastObject();

            //--> update props
            broadcast.broadcastType = BroadcastType.ACTIONS;
            broadcast.broadcastAction = BroadcastAction.GET_BACKGROUND_SERVICE_INFO;
            //-- intent propsx
            i.setAction(Globals.BROADCAST_SERVICE);
            i.putExtra(Globals.SERIALIZABLE, broadcast);

            // broadcast
            activity.sendBroadcast(i);
            return mView;
        }

        // does user exits from
        // application before summit result
        shouldShowSummaryFrame();


        return mView;
    }

    /**
     * Feature methods
     */
    private void keepTemporaryRecorder() {
        // keep temporary recorder
        AppEntity appEntity = App.instance(activity).getAppEntity();
        //appEntity.temporaryRecorder = currentRecorder;
        //appEntity.temporaryPoints = mMapUtils.points;
        //--> commit
        App.instance(activity).save(appEntity);
    }

    private void clearTemporaryRecorder() {
        // clear temporary recorder
        final AppEntity appEntity = App.instance(activity).getAppEntity();
        //--> update props
        //appEntity.temporaryRecorder = null;
        //appEntity.temporaryPoints = null;
        clearPointsInDatabase();
        clearCurrentRecorderObject();
        //--> commit
        App.instance(activity).save(appEntity);
        currentRecorder = null;
        workoutInfo = null;
    }

    private void shouldRedrawPolyline() {
        // prepare usage variables
        final String mtn = ct + "shouldRedrawPolyline() ";
        // should display summary record
        AppEntity appEntity;

        try {
            List<LatLng> points = getAllPoint();
            if (points.size() > 0) {
                if (mMapUtils != null) {
                    // update props
                    mMapUtils.points = points;

                    // redraw polyline
                    mMapUtils.redrawPolyline();

                    // zoom to fit
                    mMapUtils.zoomToFit();

                }

            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }
    }

    private void shouldShowSummaryFrame() {
        // should display summary record
        AppEntity appEntity;
        RealmRecorderObject recorderObject = getCurrentRecorderObject();
        if (recorderObject != null) {
            // update current recorder object
            currentRecorder = recorderObject;

            // display
            displaySummaryFrame(currentRecorder);

        }
    }

    private void displayOnStarted(RealmRecorderObject record) {
        // views binding
        if (record != null) {
            binding(record);

            // display resume & stop states
            // anim
            animScaleOut();

        }

    }

    private void displayOnPaused(RealmRecorderObject record) {
        // display resume & stop states
        // anim
        animScaleOut();

        // views binding
        binding(record);

    }

    private void onPoorGPSSignal() {
        final View frame = getView().findViewById(R.id.frame_gps_signal);
        final TextView lb = getView().findViewById(R.id.lb_gps_signal);
        frame.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_orange_light));
        lb.setText("POOR GPS SIGNAL");
        lb.setTextColor(ContextCompat.getColor(activity, android.R.color.white));

        frame.setVisibility(View.VISIBLE);
    }

    private void onGPSSignalLost() {
        final View frame = getView().findViewById(R.id.frame_gps_signal);
        final TextView lb = getView().findViewById(R.id.lb_gps_signal);
        frame.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_red_light));
        lb.setText("WAITING FOR GPS SIGNAL");
        lb.setTextColor(ContextCompat.getColor(activity, android.R.color.white));

        frame.setVisibility(View.VISIBLE);
    }

    private void onGPSAcquired() {
        final View frame = getView().findViewById(R.id.frame_gps_signal);
        final TextView lb = getView().findViewById(R.id.lb_gps_signal);
        frame.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_green_light));
        lb.setText("GPS ACQUIRED");
        lb.setTextColor(ContextCompat.getColor(activity, android.R.color.white));

        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.setVisibility(View.GONE);

            }
        }, 800);
    }

    private boolean isMapReady() {
        // prepare usage variables
        final String mtn = ct + "isMapReady() ";

        // map condition
        if (mMap == null) {
            // log
            L.e(mtn + "mMap[" + mMap + "] is not ready.");

            // exit from this process
            return false;
        }

        return true;
    }

    private void share() {
        // full-size map frame
        frameMap.overrideRequestLayout(1);
        // handler callback
        frameMap.post(new Runnable() {
            @Override
            public void run() {
                // capture google map preview image
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        // go to share page
                        toSharePage(currentRecorder, bitmap);

                        // reverse-size map frame
                        frameMap.overrideRequestLayout(1.1);

                    }
                });
            }
        });
    }

    private void zoomOnce(RealmRecorderObject recorder) {
        // prepare usage variables
        final String mtn = ct + "zoomOnce() ";

        // exit from this process
        if (recorder.xLoc == null || (mMap != null && mZoomOnce)) return;

        L.i(mtn + "mZoomOnce: " + mZoomOnce);

        // update flag
        mZoomOnce = true;

        // prepare usage variables
        final LatLng ll = new LatLng(recorder.xLoc.latitude, recorder.xLoc.longitude);

        // move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
    }

    private void broadcastService(BroadcastObject broadcast) {
        // prepare usage variables
        Intent i = new Intent();
        i.setAction(Globals.BROADCAST_SERVICE);
        i.putExtra(Globals.SERIALIZABLE, broadcast);

        activity.sendBroadcast(i);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private DialogInterface.OnClickListener getConfirmationDialogListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (DialogInterface.BUTTON_POSITIVE == i) {
                    // clear temporary recorder
                    clearTemporaryRecorder();

                    // clear flag
                    mOnDisplaySummary = false;

                    // to beginning
                    toBegin();

                } else dialogInterface.dismiss();

            }
        };
    }

    private void toBegin() {
        toBegin(false);
    }

    private void toBegin(boolean preventHide) {
        // reset result
        reset();

        // reset service
        resetService();

        // gone stop button
        btnStopAndSubmit.setVisibility(View.GONE);

        // change start button
        // description
        lbRecordingState.setText(getString(R.string.start_recording));

        // clear flag
        mOnDisplaySummary = false;
        mOnSubmitMultiResult = false;

        //Crea preview image
        previewImage.setImageDrawable(null);
        previewImage.setVisibility(View.GONE);

        currentRecorder = null;
        workoutInfo = null;

        // gone summary frame
        if (!preventHide) hideSummaryFrame();
    }

    private void afterShare() {
        btnSubmit.setVisibility(View.VISIBLE);
        //btnSaveWithoutSubmitResult.setVisibility(View.VISIBLE);

        // gone preview
        previewImage.setImageResource(0);

    }

    private void beforeShare() {
        btnSubmit.setVisibility(View.GONE);
        //btnSaveWithoutSubmitResult.setVisibility(View.GONE);

    }

    private AlertDialog dialogDiscardRecording(String title, String desc, DialogInterface.OnClickListener listener) {
        // prepare usage variables
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // update props
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("ยืนยัน", listener);
        builder.setNegativeButton("ยกเลิก", listener);

        // show
        return builder.create();

    }

    /**
     * Feature methods
     */
    private void toSharePage(RealmRecorderObject recorderObject, Bitmap previewImage) {
        // prepare usage variables
        SharePage page = new SharePage();

        //update props
        page.setRecorderObject(recorderObject);
        page.setPreviewMapImage(previewImage);

        // to page
        //ChildFragmentUtils.newInstance(this).addChildFragment(CONTAINER_ID, page);
        mFragmentUtils.replaceFragment(page);
    }

    private void toReviewEventPage() {
        mActiveRegisteredEventCheckerPageDialog = new ActiveRegisteredEventCheckerPage();
        mActiveRegisteredEventCheckerPageDialog.show(getChildFragmentManager(), "ReviewEvent");
    }

    private void toSuccessfullySubmitResult() {
        // display fragment
        ChildFragmentUtils.newInstance(this)
                .addChildFragment(
                        CONTAINER_ID,
                        (pageSuccessfully = new SuccessfullySubmitRunningResultPage())
                                .setFragmentHandler(new xFragmentHandler() {
                                    @Override
                                    public xFragment onResult(xTalk talk) {
                                        // xTalk to profile
                                        //xTalk x = new xTalk();
                                        //x.requestCode = Globals.RC_TO_PROFILE_PAGE;

                                        // on result
                                        //RecordPage.this.onResult(x);

                                        return null;
                                    }
                                }));
    }

    private void bindingSummary(RealmRecorderObject recorder) {
        inputDisplayTime.setText(recorder.displayRecordAsTime);
        inputDistance.setText(Globals.DCM_2.format(recorder.distanceKm));
        inputPaceDisplayTime.setText(recorder.displayPaceAsTime);
        inputCalories.setText(Globals.DCM.format(recorder.calories) + "");

        distanceInMapLabel.setText(Globals.DCM_2.format(recorder.distanceKm));
        durationInMapLabel.setText(recorder.displayRecordAsTime);
        dateTimeLabelInMap.setText(recorder.getWorkoutDate());

    }

    private void binding(RealmRecorderObject recorder) {
        lbTime.setText(recorder.displayRecordAsTime);
        lbDistance.setText(Globals.DCM_2.format(recorder.distanceKm));
        lbPace.setText(recorder.displayPaceAsTime);
        lbCalories.setText(Globals.DCM.format(recorder.calories));
    }


    private void animScaleOut() {
        AnimUtils.instance()
                .scaleOut(btnStopAndSubmit, new onAnimCallback() {
                    @Override
                    public void onEnd() {

                    }

                    @Override
                    public void onStart() {
                        btnStopAndSubmit.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void hideSummaryFrame() {
        // display recording frame
        frameRecording.setVisibility(View.VISIBLE);

        AnimUtils.instance().translateDown(frameSummary, new onAnimCallback() {
            @Override
            public void onEnd() {
            }

            @Override
            public void onStart() {
                // gone preview image
                previewImage.setImageResource(0);

                // gone usability features
                frameToolbar.setVisibility(View.GONE);
                frameDistanceInMap.setVisibility(View.INVISIBLE);
                runexLogo.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void displaySummaryFrame(RealmRecorderObject recorder) {
        // update flag
        mOnDisplaySummary = true;

        // binding views summary
        bindingSummary(recorder);

        //--> animation
        AnimUtils.instance().translateUp(frameSummary, new onAnimCallback() {
            @Override
            public void onEnd() {
            }

            @Override
            public void onStart() {

                // display usage frame
                frameToolbar.setVisibility(View.VISIBLE);
                frameDistanceInMap.setVisibility(View.VISIBLE);
                runexLogo.setVisibility(View.VISIBLE);

            }
        });

    }

    private void anim(int drawerId) {
        AnimUtils.instance().fadeOut(icRecordState, 100, 0, new onAnimCallback() {
            @Override
            public void onEnd() {
                AnimUtils.instance().fadeIn(icRecordState, 150, 0, new onAnimCallback() {
                    @Override
                    public void onEnd() {
                    }

                    @Override
                    public void onStart() {
                        Drawable icon = ContextCompat.getDrawable(getContext(), drawerId);
                        icRecordState.setImageDrawable(icon);

                    }
                });

            }

            @Override
            public void onStart() {
            }
        });
    }

    private void start() {
        // update flag
        mOnRecording = true;

        if (isMyServiceRunning(BackgroundService.class)) {
            // resume service
            resumeService();

            // start recording
        } else startService();

        // anim
        anim(R.drawable.ic_pause);

        // recording state description
        lbRecordingState.setText(getString(R.string.pause_recording));

    }

    private void reset() {
//        mRecorderUtils.reset();
        mMapUtils.clearAll();
    }

    private void paused() {
        // anim pause
        anim(R.drawable.ic_play);

        // pause recording
        pauseRecording();

        // clear flag
        mOnRecording = false;

        // scale out animation
        // condition
        if (btnStopAndSubmit.getVisibility() != View.VISIBLE) {
            // scale stop button
            animScaleOut();

        }

        // recording state description
        lbRecordingState.setText(getString(R.string.resume_recording));


    }

    private void pauseRecording() {
        // pause service
        pauseService();

        // stop service
//        stopService();

        // pause
//        mRecorderUtils.pause();
    }

    private void beforeGPSRecording() {
        // prepare usage variables
        final String mtn = ct + "beforeGPSRecording() ";
        boolean isGpsAvailable = false;

        if (mPmUtils.checkPermission(Globals.ACCESS_FINE_LOCAITON)) {
            if (isGpsAvailable = mLocUtils.isGPSAvailable()) {
                L.i(mtn + "Ready to record GPS.");

                // condition
                if (mInitMap) return;

                // update flag
                mInitMap = true;

                // init map
                initMap();

            }

        } else mPmUtils.requestPermissions(Globals.RC_PERMISSION, Globals.ACCESS_FINE_LOCAITON);


        if (!isGpsAvailable) L.i(mtn + "isGPSAvailable(" + isGpsAvailable + ") ");
    }

    /**
     * API methods
     */
    private void apiSubmitMultiEvents(WorkoutInfo workoutInfo,
                                      List<EventIdAndPartnerObject> selectedEvents, File file) {
        // prepare usage variables
        final String mtn = ct + "apiSubmitMultiEvents() ";

        // update flag
        onNetwork = true;
        mOnSubmitMultiResult = true;

        final rqSubmitActivitiesWorkout request = new rqSubmitActivitiesWorkout();

        // update props
        request.setEvent_activity(selectedEvents);
        request.setWorkout_info(workoutInfo);

        // fire
        new SubmitActivitiesWorkoutService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);

                if (response.responseCode == HttpURLConnection.HTTP_OK) {

                    // remove fragment dialog
                    mActiveRegisteredEventCheckerPageDialog.dismissAllowingStateLoss();

                    // clear flag
                    onNetwork = false;
                    mOnSubmitMultiResult = false;

                    stopService();
                    // binding views
                    binding(new RealmRecorderObject());
                    // to begin step
                    toBegin();

                    // to success page
                    toSuccessfullySubmitResult();

                    // clear temporary recorder
                    clearTemporaryRecorder();

                    previewImage.setImageDrawable(null);
                    previewImage.setVisibility(View.GONE);
                    frameSummary.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);
                // clear flag
                onNetwork = false;
                mOnSubmitMultiResult = false;

                // remove fragment dialog
                mActiveRegisteredEventCheckerPageDialog.dismissAllowingStateLoss();

                previewImage.setImageDrawable(null);
                previewImage.setVisibility(View.GONE);

            }
        }).doIt(request, file);

    }

//    private void apiSaveRecord(onNetworkCallback callback) {
//        // prepare usage variables
//        final String mtn = ct + "apiSaveRecord() ";
//        final rqAddRunningHistory request = new rqAddRunningHistory();
//        final double recordTime = recTimeAsMin(currentRecorder.durationMillis);
//        final double recordPace = paceAsMin(currentRecorder.paceMillis);
//
//
//        request.setActivity_type(Globals.ACTIVITY_RUN);
//        request.setCalory(currentRecorder.calories);
//        request.setDistance(currentRecorder.distanceKm);
//        request.setCaption("");
//        request.setImage_path("");
//        request.setPace(recordPace);
//        request.setTime(recordTime);
//
//        L.i(mtn + "save record: " + Globals.GSON.toJson(request));
//        new AddHistoryService(activity, new onNetworkCallback() {
//            @Override
//            public void onSuccess(xResponse response) {
//                L.i(mtn + "successfully");
//                L.i(mtn + "response: " + response.jsonString);
//
//                if (callback == null) {
//                    // clear temporary recorder
//                    clearTemporaryRecorder();
//
//                    // clear flag
//                    onNetwork = false;
//
//                    // successfully submit result
//                    // to begin step
//                    toBegin();
//
//                    // prepare usage variables
//                    xTalk x = new xTalk();
//                    x.requestCode = Globals.RC_TO_PROFILE_PAGE;
//
//                    // on result
//                    onResult(x);
//
//                } else callback.onSuccess(response);
//            }
//
//            @Override
//            public void onFailure(xResponse response) {
//                L.i(mtn + "failure");
//                L.i(mtn + "response: " + response.jsonString);
//
//                // clear flag
//                onNetwork = false;
//
//                // callback
//                if (callback != null) callback.onFailure(response);
//            }
//        }).doIt(request);
//    }

    private void apiWorkOuts(onNetworkCallback callback) {
        final String mtn = ct + "apiWorkOuts() ";
        final rqAddWorkOutsHistory request = new rqAddWorkOutsHistory();

        final long recordTime = recTimeAsSec(currentRecorder.durationMillis);
        final double recordPace = paceAsMin(currentRecorder.paceMillis);

        //request.setActivity_type(Globals.ACTIVITY_RUN);
        request.setCalory(currentRecorder.calories);
        request.setCaption("");
        request.setDistance(currentRecorder.distanceKm);
        request.setDuration(recordTime);
        request.setTime_string(currentRecorder.displayRecordAsTime);

        List<RealmPointObject> locations = realm.copyFromRealm(realm.where(RealmPointObject.class).findAll());
        if (locations.size() > 0) {
            String startDateTime = locations.get(0).getTimestamp();
            String endDateTime = locations.get(locations.size() - 1).getTimestamp();

            request.setStart_date(startDateTime);
            request.setWorkout_date(startDateTime);
            request.setEnd_date(endDateTime);
        }

        request.setLocations(locations);
        request.setPace(recordPace);
        onNetwork = true;
        new AddWorkOutsService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "successfully");
                L.i(mtn + "response: " + response.jsonString);

                WorkoutObject workoutObject = Globals.GSON.fromJson(response.jsonString, WorkoutObject.class);
                workoutInfo = workoutObject.getData();

                // clear flag
                onNetwork = false;

                // successfully submit result
                // to begin step
                //toBegin();

                //prepare usage variables
                //xTalk x = new xTalk();
                //x.requestCode = Globals.RC_TO_PROFILE_PAGE;

                // on result
                //onResult(x);

                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.i(mtn + "failure");
                L.i(mtn + "response: " + response.jsonString);

                // clear flag
                onNetwork = false;

                // callback
                if (callback != null) {
                    callback.onFailure(response);
                }

            }
        }).doIt(request);
    }

    @Override
    public void onConfirmEvents(List<EventIdAndPartnerObject> selectedEvents) {
        //TODO("Send distance from event id")
        // prepare usage variables
        final String mtn = ct + "onConfirmEvents() ";

        // condition
        if (mOnSubmitMultiResult) return;

        // condition
        if (selectedEvents != null) {
            L.i(mtn + "going to save record.");
            this.selectedEvents = selectedEvents;
            saveMapToDeviceSubmitMultiEvents();
        }
    }

    private void saveMapToDeviceSubmitMultiEvents() {
        if (!requestPermissionsWriteStoragePermission()) return;

        //
        // full-size map frame
        frameMap.overrideRequestLayout(1);
        // handler callback
        frameMap.post(new Runnable() {
            @Override
            public void run() {
                // capture google map preview image
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {

                        previewImage.setImageBitmap(bitmap);
                        previewImage.setVisibility(View.VISIBLE);

                        // snap and save as file
                        File file = DeviceUtils.instance(getContext()).takeScreenshot2(getContext(), R.id.frame_map);

                        if (workoutInfo != null) {
                            apiSubmitMultiEvents(workoutInfo, selectedEvents, file);
                        } else {
                            // save before submit multi events
                            apiWorkOuts(new onNetworkCallback() {
                                @Override
                                public void onSuccess(xResponse response) {
                                    WorkoutObject workoutObject = Globals.GSON.fromJson(response.jsonString, WorkoutObject.class);
                                    workoutInfo = workoutObject.getData();
                                    //L.i(mtn + "going to submit multiple events.");
                                    apiSubmitMultiEvents(workoutInfo, selectedEvents, file);
                                }

                                @Override
                                public void onFailure(xResponse response) {
                                    // clear flag
                                    mOnSubmitMultiResult = false;
                                    previewImage.setImageDrawable(null);
                                    previewImage.setVisibility(View.GONE);
                                }
                            });
                        }

                        // reverse-size map frame
                        frameMap.overrideRequestLayout(1.1);
                    }
                });
            }
        });
    }

    /**
     * Feature methods
     */
    private boolean isGranted(String permissionName) {
        return PermissionUtils.newInstance(this).checkPermission(permissionName);
    }

    private boolean requestPermissionsWriteStoragePermission() {
        if (!isGranted(Globals.READ_EXTERNAL_STORAGE) || !isGranted(Globals.WRITE_EXTERNAL_STORAGE)) {
            // request
            PermissionUtils.newInstance(this).requestPermissions(RC_PERMISSION_SAVE_FILE,
                    new String[]{Globals.READ_EXTERNAL_STORAGE, Globals.WRITE_EXTERNAL_STORAGE});

            // exit from this process
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_SAVE_FILE) {
            boolean allGranted = true;
            for (int a = 0; a < grantResults.length; a++) {
                L.i("AAA: " + grantResults[a]);

                if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                saveMapToDeviceSubmitMultiEvents();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnStart.setOnClickListener(this);
        btnStopAndSubmit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        //btnSaveWithoutSubmitResult.setOnClickListener(this);
        //frameChangeBgImage.setOnClickListener(this);

        //--> Frame share
        btnCancelSubmit.setOnClickListener(this);
        btnSocialShare.setOnClickListener(this);
    }

    /**
     * Matching views
     */
    private void viewMatching(View v) {
        // lab
        labAccuracy = v.findViewById(R.id.lab_accuracy);
        frameDistanceInMap = v.findViewById(R.id.frame_distance_in_map);
        distanceInMapLabel = v.findViewById(R.id.lb_distance_2);
        durationInMapLabel = v.findViewById(R.id.lb_duration);
        dateTimeLabelInMap = v.findViewById(R.id.lb_date_time);
        runexLogo = v.findViewById(R.id.runex_logo);

        lbPace = v.findViewById(R.id.lb_pace);
        lbDistance = v.findViewById(R.id.lb_distance);
        lbTime = v.findViewById(R.id.lb_time);
        lbCalories = v.findViewById(R.id.lb_calories);

        lbRecordingState = v.findViewById(R.id.lb_recording_state_description);
        btnStart = v.findViewById(R.id.btn_start);
        btnStopAndSubmit = v.findViewById(R.id.frame_stop_and_submit);
        //btnSaveWithoutSubmitResult = v.findViewById(R.id.btn_save_without_submit_result);
        icRecordState = v.findViewById(R.id.ic_recording_state);

        //--> Frame map
        frameMap = v.findViewById(R.id.map);

        //--> Frame share
        frameToolbar = v.findViewById(R.id.frame_toolbar);
        btnCancelSubmit = v.findViewById(R.id.btn_cancel_submit);
        btnSocialShare = v.findViewById(R.id.btn_share);

        //--> Frame recording
        frameRecording = v.findViewById(R.id.frame_recording);

        //--> Frame change background image
        previewImage = v.findViewById(R.id.preview_image);
        //frameChangeBgImage = v.findViewById(R.id.frame_change_background_image);

        //--> Summary views
        frameSummary = v.findViewById(R.id.inherit_frame_summary);
        inputDistance = frameSummary.findViewById(R.id.lb_distance);
        inputDisplayTime = frameSummary.findViewById(R.id.lb_time);
        inputPaceDisplayTime = frameSummary.findViewById(R.id.lb_step);
        inputCalories = frameSummary.findViewById(R.id.lb_calories);
        btnSubmit = frameSummary.findViewById(R.id.btn_submit_result);
    }

    /**
     * Initial GoogleMap
     */
    private void initMap() {
        // prepare usage variables
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        // transaction
        fm.beginTransaction()
                .replace(frameMap.getId(), mapFragment)
                .commit();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Life cycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // prepare usage variables
        final String mtn = ct + "onCreate() ";

        L.i(mtn + "is BackgroundService running: " + isMyServiceRunning(BackgroundService.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";

        Toast.makeText(activity, "HELLO", Toast.LENGTH_SHORT).show();

        if (requestCode == Globals.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                // prepare usage variables
                InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

//                String strUri = ImageUtils.getRealPathFromURI(activity, data.getData());
//                L.i(mtn +"strUri: "+ strUri);

                // display preview image
                previewImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // stop background service
//        stopService();

        // unregister
        activity.unregisterReceiver(testBroadcastReceiver);

        // prepare usage variables
        final String mtn = ct + "onDestroy() ";

        // log
        L.i(mtn);

    }

    @Override
    public void onStop() {
        super.onStop();

        // prepare usage variables
        final String mtn = ct + "onStop() ";
        // log
        L.i(mtn);

    }

    @Override
    public void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct + "onResume() ";

        // register broadcast
        activity.registerReceiver(testBroadcastReceiver, new IntentFilter(Globals.BROADCAST_TEST));

        // log
        L.i(mtn);

        // conditions
        beforeGPSRecording();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //--> Location utils
        mLocUtils = LocationUtils.newInstance(activity);
        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(activity);

        // view change listener
        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // remove listener
                btnStopAndSubmit.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // show
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frameRecording.setVisibility(View.VISIBLE);

                    }
                }, 120);

                // gone
                btnStopAndSubmit.setVisibility(View.GONE);

                // hide frame summary
                frameSummary.setY(frameSummary.getY() + frameSummary.getHeight());


            }
        });

        // handler back pressed
//        handlerBackPressed("onViewCreated", mView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        // when visible
//        if (!hidden) handlerBackPressed("onHiddenChanged", getView());
    }

    public void handlerBackPressed(String tag, View view) {
        // prepare usage variables
        final String mtn = ct + "handlerBackPressed(" + tag + ") ";

        //-> Handler back button
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                L.i(mtn + "Child count: " + getChildFragmentCount());
                // on back pressed
                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                    // conditions
                    if (i != KeyEvent.KEYCODE_BACK) return false;
                    if (getChildFragmentCount() <= 1 && mOnDisplaySummary) {

                        // prepare usage variables
                        final DialogInterface.OnClickListener listener = getConfirmationDialogListener();

                        // confirmation dialog
                        dialogDiscardRecording("ยืนยัน",
                                "คุณต้องการออกจากหน้านี้โดยไม่บันทึกผลการวิ่ง ?",
                                listener).show();

                        // exit from this process
                        return true;

                    } else if (getChildFragmentCount() > 1) {
                        // get latest fragment
                        final Fragment fragment = getChildFragments().get(getChildFragmentCount() - 1);

                        // remove fragment
                        getChildFragmentManager().beginTransaction()
                                .remove(fragment)
                                .commit();

                        return true;
                    }
                }

                return false;
            }
        });
    }


    /**
     * Micro methods
     */
    public double recTimeAsMin(long recordDurationMillis) {
        // prepare usage variables
        final String mtn = ct + "recTimeAsMin() ";

        try {
            double toSec = recordDurationMillis / 1000;
            double toMin = toSec / 60;

            L.i(mtn + "record time: " + recordDurationMillis);
            L.i(String.format(mtn + "record time as min: " + toMin + ""));

            return toMin;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }

    /**
     * Micro methods
     */
    public long recTimeAsSec(long recordDurationMillis) {
        // prepare usage variables
        final String mtn = ct + "recTimeAsSec() ";

        try {
            long toSec = recordDurationMillis / 1000;


            L.i(mtn + "record time: " + recordDurationMillis);
            L.i(String.format(mtn + "record time as sec: " + toSec + ""));

            return toSec;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }

    public double paceAsMin(long paceMillis) {
        // prepare usage variables
        final String mtn = ct + "paceAsMin() ";

        try {
            final double toSec = paceMillis / 1000;
            final double toMin = toSec / 60;

            if (paceMillis <= 0 || Double.isNaN(toMin)) return 0;

            return toMin;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }

        return 0;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null) {
            //clearPointsInDatabase();
            realm.close();
        }
    }

    private List<LatLng> getAllPoint() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        List<RealmPointObject> points = realm.copyFromRealm(realm.where(RealmPointObject.class).findAll());
        for (RealmPointObject point : points) {
            latLngs.add(point.toLatLng());
        }
        return latLngs;
    }

    private void clearPointsInDatabase() {
        realm.beginTransaction();
        realm.delete(RealmPointObject.class);
        realm.commitTransaction();
    }

    private RealmRecorderObject getCurrentRecorderObject() {
        if (realm == null) {
            return null;
        }
        return realm.where(RealmRecorderObject.class).findFirst();
    }


    private void clearCurrentRecorderObject() {
        if (realm == null) {
            return;
        }
        realm.beginTransaction();
        realm.delete(RealmRecorderObject.class);
        realm.commitTransaction();
    }
}
