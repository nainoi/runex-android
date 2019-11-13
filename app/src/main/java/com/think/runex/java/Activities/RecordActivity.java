package com.think.runex.java.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Pages.ReviewEvent.OnConfirmEventsListener;
import com.think.runex.java.Pages.ReviewEvent.ReviewEventPage;
import com.think.runex.java.Pages.SuccessfullySubmitRunningResultPage;
import com.think.runex.java.Services.BackgroundService;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.Animation.AnimUtils;
import com.think.runex.java.Utils.Animation.onAnimCallback;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.GoogleMap.GoogleMapUtils;
import com.think.runex.java.Utils.GoogleMap.xLocation;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.Network.Request.rqSubmitMultiEvents;
import com.think.runex.java.Utils.Network.Services.SubmitMultiEventsService;
import com.think.runex.java.Utils.Network.Services.SubmitRunningResultService;
import com.think.runex.java.Utils.Network.Request.rqAddRunningHistory;
import com.think.runex.java.Utils.Network.Request.rqSubmitRunningResult;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.AddHistoryService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.Recorder.RecorderUtils;
import com.think.runex.java.Utils.Recorder.onRecorderCallback;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;

public class RecordActivity extends xActivity implements OnMapReadyCallback
        , View.OnClickListener
        , onRecorderCallback
        , OnConfirmEventsListener {
    /**
     * Main variables
     */
    private final String ct = "RecordActivity->";

    // instance variables
    private CallbackManager callbackManager;
    private FragmentUtils mFUtils;
    private LocationUtils mLocUtils;
    private PermissionUtils mPmUtils;
    private GoogleMapUtils mMapUtils;
    private GoogleMap mMap;
    private xLocation mLastLocation;
    private RecorderUtils mRecorderUtils;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // prepare usage variables
        final String mtn = ct + "BroadcastReceiver() ";

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // prepare usage variables
                final String jsonString = intent.getStringExtra(Globals.BROADCAST_LOCATION_VAL);
                final xLocation location = Globals.GSON.fromJson(jsonString, xLocation.class);

                // on location changed
                locationChanged(location);

            } catch (Exception e) {
                e.printStackTrace();
                L.e(mtn + "Err: " + e.getMessage());
            }
        }
    };

    // explicit variables
    private boolean onNetwork = false;
    private boolean mZoomOnce = false;
    private boolean mOnRecording = false;
    private boolean mInitMap = false;
    private final int CONTAINER_ID = R.id.display_fragment_frame;
    private boolean mOnDisplaySummary = false;
    //--> XRequest code
    private final int XRC_REMOVE_ALL_FRAGMENT = 1001;

    // views
    private TextView lbRecordingState;
    private ImageView icRecordState;
    private TextView lbTime;
    private TextView lbDistance;
    private TextView lbCalories;
    private TextView lbPace;
    private View frameLogo;
    //-->Labs
    private TextView labAccuracy;
    //--> Frame map
    private View frameMap;
    //--> Frame share
    private View frameShare;
    private View btnSocialShare;
    //--> Frame recording
    private View frameRecording;

    private TextView btnStart;
    private TextView btnSaveWithoutSubmitResult;
    private View btnStopAndSubmit;
    private ImageView previewImage;
    //--> Toolbar
    private View btnCancelSubmit;
    //--> Change background image
    private View frameChangeBgImage;
    //--> Running summary frame
    private View frameSummary;
    private TextView inputDistance, inputDisplayTime, inputPaceDisplayTime, inputCalories;
    private View btnSubmit;

    @Override
    public void onBackPressed() {
        if (mFUtils.getStackCount() > 0) getSupportFragmentManager().popBackStack();
        else if (mOnDisplaySummary) {
            // clear flag
            mOnDisplaySummary = false;

            // hide summary frame
            hideSummaryFrame();

//        } else if(mRecorderUtils.mRecordDistanceKm > 0L ){
//            // display discard recording
//            dialogDiscardRecording();

        } else super.onBackPressed();
    }


    /**
     * Implement methods
     */
    @Override
    public void onFragmentCallback(xTalk xTalk) {
        // remove all fragment page
        if (xTalk.requestCode == XRC_REMOVE_ALL_FRAGMENT) {

            // exit from this activity
            finish();

//            // remove all fragment pages
//            fragmentManager.removeAllFragment();
//
//            // reset result
//            reset();
//
//            // refresh views
//            binding();
//
//            // hide stop button
//            btnStopAndSubmit.setVisibility(View.GONE);
//
//            // change start button
//            // description
//            lbRecordingState.setText(getString(R.string.start_recording));
//
//            // clear flag
//            mOnDisplaySummary = false;
//
//            // hide summary frame
//            hideSummaryFrame();

        }

    }

    @Override
    public void onRecordTimeChanged(String time) {
        binding();

    }

    @Override
    public void onClick(View view) {
        final String mtn = ct + "onClick() ";

        switch (view.getId()) {
            case R.id.btn_cancel_submit: onBackPressed(); break;
            case R.id.btn_share:
                // before share
                beforeShare();

                frameSummary.post(new Runnable() {
                    @Override
                    public void run() {

                        // capture google map preview image
                        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(Bitmap bitmap) {
                                // display map preview
                                previewImage.setImageBitmap( bitmap );

                                // share with facebook
                                shareWithFacebook();
                            }
                        });

                    }
                });

                // after share
//                afterShare();

                break;
            case R.id.btn_save_without_submit_result:
                if( onNetwork ) return;

                // update flag
                onNetwork = true;

                // save record without submit
                apiSaveRecord(false);

                break;

            case R.id.btn_submit_result:

                // condition
                if (onNetwork) return;

                // update flag
                onNetwork = true;

                // save record with submit
                apiSaveRecord( true );

                break;

            case R.id.frame_change_background_image:
                // open image gallery picker
                DeviceUtils.instance(this).openImagePicker(this, Globals.RC_PICK_IMAGE);

                break;

            case R.id.btn_start:
                // going to stop recording
                if (mOnRecording) {
                    // log
                    L.i(mtn + "on recording. -> going to pause recording.");

                    // paused
                    paused();

                    // exit from this process
                    return;
                }

                // start
                start();

                break;

            case R.id.frame_stop_and_submit:
                // paused
                paused();

                // prepare recording object
                RecorderObject recorderObj = new RecorderObject();
                recorderObj.setDistanceKm(mRecorderUtils.mRecordDistanceKm);
                recorderObj.setRecordingTime(mRecorderUtils.mRecordTime);
                recorderObj.setRecordingDisplayTime(mRecorderUtils.mRecordDisplayTime);
                recorderObj.setCalories(mRecorderUtils.mRecordCalories);
                recorderObj.setRecordingPaceDisplayTime(mRecorderUtils.mRecordPaceDisplayTime);

                // display confirm stop recording
                dialogDiscardRecording("ยืนยัน", "คุณยืนยันที่จะสิ้นสุดการวิ่ง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // conditions
                        if (DialogInterface.BUTTON_POSITIVE == i) {
                            // update flag
                            mOnDisplaySummary = true;

                            // zoom to fit
                            mMapUtils.zoomToFit();

                            // display summary frame
                            displaySummaryFrame(recorderObj);

                            // dismiss
                        } else dialogInterface.dismiss();

                    }
                }).show();


                // display change background image
//                frameChangeBgImage.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * Service methods
     */
    public void startService() {
        // use this to start and trigger a service
        Intent i = new Intent(this, BackgroundService.class);
        // potentially add data to the intent
        i.putExtra("KEY1", "Value to be used by the service");

        startService(i);
    }

    // Stop the service
    public void stopService() {
        stopService(new Intent(this, BackgroundService.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // prepare usage variables
        final String mtn = ct + "onMapRead() ";

        // update props
        mMap = googleMap;

        //--> Map utils
        mMapUtils = GoogleMapUtils.newInstance(this, mMap);
        //--> Recorder utils
        mRecorderUtils = RecorderUtils.newInstance(this);
        mRecorderUtils.setRecorderCallback(this);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);

        mMap.setMyLocationEnabled(true);

        // prepare usage variables
        Location lkLoc = mLocUtils.getLastKnownLocation();

        // last known not found
        if (lkLoc != null) {
            // update flag
            mZoomOnce = true;

            // prepare usage variables
            LatLng ll = new LatLng(lkLoc.getLatitude(), lkLoc.getLongitude());

            // update map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));

        }
//
        xLocation x1 = new xLocation(13.845689, 100.596905);
        xLocation x2 = new xLocation(13.845585, 100.594587);
        xLocation x3 = new xLocation(13.842551, 100.595622);
        xLocation x4 = new xLocation(13.843457, 100.597479);

        locationChanged(x1);
        locationChanged(x2);
        locationChanged(x3);
        locationChanged(x4);

        new Thread(new Runnable() {

            double l1 = -0.00003;
            double l2 = -0.00003;

            @Override
            public void run() {
                while( true ){

                    try{
                        Thread.sleep(1200);

                        RecordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                l1 += 0.000003;
                                l2 += 0.000003;
                                xLocation x4 = new xLocation(13.843457 + l1 , 100.597479 + l2);
                                locationChanged(x4);



                            }
                        });
                    }catch ( Exception e ){
                        L.e(mtn +"Err: "+ e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // init facebook sdk
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        // prepare usage variables
        final String mtn = ct + "onCreate() ";

        // views matching
        viewMatching();

        // view event listener
        viewEventListener();

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance(this);
        actUtls.fullScreen();
        //--> Fragment utils
        mFUtils = FragmentUtils.newInstance(this, CONTAINER_ID);

        //--> Location utils
        mLocUtils = LocationUtils.newInstance(this);
        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(this);

        // view change listener
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // hide
                btnStopAndSubmit.setVisibility(View.GONE);
                frameSummary.setY(frameSummary.getY() + frameSummary.getHeight());

                // remove listener
                btnStopAndSubmit.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

    }

    /**
     * Feature methods
     */
    private void afterShare(){
        btnSubmit.setVisibility(View.VISIBLE);
        btnSaveWithoutSubmitResult.setVisibility(View.VISIBLE);

        // hide preview
        previewImage.setImageResource( 0 );

    }
    private void beforeShare(){
        btnSubmit.setVisibility(View.GONE);
        btnSaveWithoutSubmitResult.setVisibility(View.GONE);

    }
    private void shareWithFacebook(){
        // prepare usage variables
        ShareDialog shareDialog = new ShareDialog(this);
        File file = DeviceUtils.instance(this).takeScreenshot(this, R.id.full_view_display);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        // update props
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                // after share
                afterShare();
            }

            @Override
            public void onCancel() {
                afterShare();

            }

            @Override
            public void onError(FacebookException error) {
                afterShare();

            }
        });
        // share to facebook
        shareDialog.show(content);
    }
    private AlertDialog dialogDiscardRecording(String title, String desc, DialogInterface.OnClickListener listener) {
        // prepare usage variables
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
    ReviewEventPage mReviewEventPageDialog;

    private void toReviewEventPage() {
        mReviewEventPageDialog = new ReviewEventPage();
        mReviewEventPageDialog.show(getSupportFragmentManager(), "ReviewEvent");
    }

    private void toSuccessfullySubmitResult() {
        // prepare usage variables
        xTalk x = new xTalk();
        x.requestCode = XRC_REMOVE_ALL_FRAGMENT;

        // display fragment
        mFUtils.replaceFragment(new SuccessfullySubmitRunningResultPage().setRequestCode(x));
    }

    private void locationChanged(xLocation location) {
        // prepare usage variables
        final String mtn = ct + "locationChanged() ";

        if (mLastLocation == null) {
            // keep last location
            mLastLocation = new xLocation(location.latitude, location.longitude);

            // exit from this process
            return;

        }
        if (mMap == null) {
            // log
            L.i(mtn + "mMap[" + mMap + "] is not ready.");

            // exit from this process
            return;
        }

        L.i(mtn + "Location: " + location.latitude + ", " + location.longitude);

        // prepare usage variables
        LatLng ll = new LatLng(location.latitude, location.longitude);
        xLocation xFrom = mLastLocation;
        xLocation xTo = new xLocation(location.latitude, location.longitude);

        // test add polyline
        mMapUtils.addPolyline(xFrom, xTo);
        mMapUtils.addDistance(xFrom, xTo);
        mRecorderUtils.addDistance(mMapUtils.difDistance(xFrom, xTo));

        // update view
        binding();

        // lab views
        labAccuracy.setText("accuracy: "+ location.accuracy);

        // update props
        mLastLocation = xTo;

        if (!mZoomOnce) {
            // update flag
            mZoomOnce = true;

            // move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
        }
    }

    private void bindingSummary(RecorderObject recorder) {
        inputDisplayTime.setText(recorder.recordingDisplayTime);
        inputDistance.setText(Globals.DCM.format(recorder.distanceKm) + "km");
        inputPaceDisplayTime.setText(recorder.recordingPaceDisplayTime);
        inputCalories.setText(Globals.DCM.format(recorder.calories )+ "");

    }

    private void binding() {
        lbTime.setText(mRecorderUtils.mRecordDisplayTime);
        lbDistance.setText(Globals.DCM.format(mRecorderUtils.mRecordDistanceKm) + "km");
        lbPace.setText(mRecorderUtils.mRecordPaceDisplayTime);
        lbCalories.setText( Globals.DCM.format(mRecorderUtils.mRecordCalories) );

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
                // change layout above
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameMap.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE, frameRecording.getId());

            }

            @Override
            public void onStart() {
                // hide preview image
                previewImage.setImageResource( 0 );

                // hide usability features
                frameShare.setVisibility(View.INVISIBLE);
                frameLogo.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void displaySummaryFrame(RecorderObject recorder) {
        // binding views summary
        bindingSummary(recorder);

        //--> animation
        AnimUtils.instance().translateUp(frameSummary, new onAnimCallback() {
            @Override
            public void onEnd() {
                // change layout above
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameMap.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE, frameSummary.getId());

                // hide recording frame
                frameRecording.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onStart() {

                // display usage frame
                frameShare.setVisibility(View.VISIBLE);
                frameLogo.setVisibility(View.VISIBLE);

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
                        icRecordState.setImageDrawable(getDrawable(drawerId));

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

        // start recording
        startRecording();

        // anim
        anim(R.drawable.ic_pause);

        // recording state description
        lbRecordingState.setText(getString(R.string.pause_recording));

    }

    private void reset() {
        mRecorderUtils.reset();
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

    private void startRecording() {
        // start recording
        mRecorderUtils.start();

        // start service
        startService();
    }

    private void pauseRecording() {
        // stop service
        stopService();

        // pause
        mRecorderUtils.pause();
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
    private void apiSubmitMultiEvents(String[] eventIds) {
        // prepare usage variables
        final String mtn = ct + "apiSubmitMultiEvents() ";
        final rqSubmitMultiEvents request = new rqSubmitMultiEvents();
        final double recordTime = mRecorderUtils.recTimeAsMin();

        // update props
        request.setCalory(mRecorderUtils.mRecordCalories);
        request.setDistance(mRecorderUtils.mRecordDistanceKm);
        request.setTime(recordTime);
        request.setCaption("");
        request.setEvent_id(eventIds);

        // fire
        new SubmitMultiEventsService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);

                if (response.responseCode == HttpURLConnection.HTTP_OK) {

                    // remove fragment dialog
                    mReviewEventPageDialog.dismissAllowingStateLoss();

                    // reset result
                    reset();

                    // refresh views
                    binding();

                    // hide stop button
                    btnStopAndSubmit.setVisibility(View.GONE);

                    // change start button
                    // description
                    lbRecordingState.setText(getString(R.string.start_recording));

                    // clear flag
                    mOnDisplaySummary = false;

                    // hide summary frame
                    hideSummaryFrame();

                    // to success page
                    toSuccessfullySubmitResult();
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // remove fragemnt dialog
                mReviewEventPageDialog.dismissAllowingStateLoss();

            }
        }).doIt(request);

    }

    private void apiSaveRecord(boolean withSubmit) {
        // prepare usage variables
        final String mtn = ct + "apiSaveRecord() ";
        final rqAddRunningHistory request = new rqAddRunningHistory();
        final double recordTime = mRecorderUtils.recTimeAsMin();
        final double recordPace = mRecorderUtils.paceAsMin();

        request.setActivity_type(Globals.ACTIVITY_RUN);
        request.setCalory(0);
        request.setDistance(mRecorderUtils.mRecordDistanceKm);
        request.setCaption("");
        request.setImage_path("");
        request.setPace(recordPace);
        request.setTime(recordTime);

        L.i(mtn + "save record: " + Globals.GSON.toJson(request));
        new AddHistoryService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "successfully");
                L.i(mtn + "response: " + response.jsonString);

                // clear flag
                onNetwork = false;

                // successfully submit result
                if( withSubmit) toReviewEventPage();
                else {

                    // update activity result
                    setResult(Activity.RESULT_OK);

                    // exit from this activity
                    finish();
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.i(mtn + "failure");
                L.i(mtn + "response: " + response.jsonString);

                // clear flag
                onNetwork = false;
            }
        }).doIt(request);
    }

    @Override
    public void onConfirmEvents(String[] selectedEvents) {
        //TODO("Send distance from event id")
        if (selectedEvents != null) {

            // update multiple event
            apiSubmitMultiEvents(selectedEvents);

        }
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnStart.setOnClickListener(this);
        btnStopAndSubmit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnSaveWithoutSubmitResult.setOnClickListener(this);
        frameChangeBgImage.setOnClickListener(this);

        //--> Frame share
        btnCancelSubmit.setOnClickListener(this);
        btnSocialShare.setOnClickListener(this);
    }

    /**
     * Matching views
     */
    private void viewMatching() {
        // lab
        labAccuracy = findViewById(R.id.lab_accuracy);
        frameLogo = findViewById(R.id.frame_logo);

        lbPace = findViewById(R.id.lb_pace);
        lbDistance = findViewById(R.id.lb_distance);
        lbTime = findViewById(R.id.lb_time);
        lbCalories = findViewById(R.id.lb_calories);

        lbRecordingState = findViewById(R.id.lb_recording_state_description);
        btnStart = findViewById(R.id.btn_start);
        btnStopAndSubmit = findViewById(R.id.frame_stop_and_submit);
        btnSaveWithoutSubmitResult = findViewById(R.id.btn_save_without_submit_result);
        icRecordState = findViewById(R.id.ic_recording_state);

        //--> Frame map
        frameMap = findViewById(R.id.frame_map);

        //--> Frame share
        frameShare = findViewById(R.id.frame_toolbar);
        btnCancelSubmit = findViewById(R.id.btn_cancel_submit);
        btnSocialShare = findViewById(R.id.btn_share);

        //--> Frame recording
        frameRecording = findViewById(R.id.frame_recording);

        //--> Frame change background image
        previewImage = findViewById(R.id.preview_image);
        frameChangeBgImage = findViewById(R.id.frame_change_background_image);

        //--> Summary views
        frameSummary = findViewById(R.id.frame_summary);
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Life cycle
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";

        if (requestCode == Globals.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                // prepare usage variables
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

//                String strUri = ImageUtils.getRealPathFromURI(this, data.getData());
//                L.i(mtn +"strUri: "+ strUri);

                // display preview image
                previewImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop background service
        stopService();

        // unregister
        unregisterReceiver(broadcastReceiver);

        // prepare usage variables
        final String mtn = ct + "onDestroy() ";

        // log
        L.i(mtn);

    }

    @Override
    protected void onStop() {
        // prepare usage variables
        final String mtn = ct + "onStop() ";

        // log
        L.i(mtn);
//        mLocUtils.stop();

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct + "onResume() ";

        // register broadcast
        registerReceiver(broadcastReceiver, new IntentFilter("BROADCAST"));

        // log
        L.i(mtn);

        // conditions
        beforeGPSRecording();

    }

}
