package com.think.runex.java.Pages;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.UiSettings;
import com.google.android.libraries.maps.model.LatLng;
import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.Views.xMapViewGroup;
import com.think.runex.java.Models.EventIdAndPartnerObject;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Pages.ReviewEvent.ActiveRegisteredEventCheckerPage;
import com.think.runex.java.Pages.ReviewEvent.OnConfirmEventsListener;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.GoogleMap.GoogleMapUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Location.LocationUtils;
import com.think.runex.java.Utils.Network.Request.rqSubmitActivitiesWorkout;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.SubmitActivitiesWorkoutService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.RxBus;
import com.think.runex.java.event.RefreshEvent;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.List;

import static com.think.runex.java.Constants.Globals.RC_PERMISSION_SAVE_FILE;

public class WorkoutDetailPage extends xActivity implements OnMapReadyCallback, OnConfirmEventsListener {

    /**
     * Main variables
     */
    private final String ct = "WorkoutDetailPage->";

    private int CONTAINER_ID;

    // instance variables
    private ActiveRegisteredEventCheckerPage mActiveRegisteredEventCheckerPageDialog;
    // instance variables
    private FragmentUtils mFragmentUtils;

    //--> Toolbar
    private View btnClose;
    //--> Frame share
    private View frameShare;
    private View btnSocialShare;

    //--> Frame map
    private xMapViewGroup frameMap;
    private ImageView runexLogo;
    private TextView distanceInMapLabel, durationInMapLabel, dateTimeLabelInMap;
    private ImageView previewImage;

    private TextView lbDistance;
    private TextView lbTime;
    private TextView inputPaceDisplayTime;
    private TextView lbCalories;
    private View btnSubmit;

    private PermissionUtils mPmUtils;
    private LocationUtils mLocUtils;
    private GoogleMapUtils mMapUtils;
    private GoogleMap mMap;

    private boolean mOnSubmitMultiResult = false;
    private WorkoutInfo workoutInfo;

    private List<EventIdAndPartnerObject> selectedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutInfo = getIntent().getParcelableExtra("workoutInfo");
        //--> Location utils
        mLocUtils = LocationUtils.newInstance(this);
        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(this);

        setContentView(R.layout.page_workout_detail);
        CONTAINER_ID = R.id.display_fragment_frame;

        // Fragment inits
        mFragmentUtils = FragmentUtils.newInstance(this, CONTAINER_ID);

        // views matching
        viewMatching();

        // view event listener
        viewEventListener();

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance(this);
        actUtls.fullScreen();

        initMap();
        updateProps();
    }

    private void viewMatching() {
        btnClose = findViewById(R.id.btn_cancel_submit);
        btnSocialShare = findViewById(R.id.btn_share);

        //--> Frame map
        frameMap = findViewById(R.id.map);
        runexLogo = findViewById(R.id.runex_logo);
        distanceInMapLabel = findViewById(R.id.lb_distance_2);
        durationInMapLabel = findViewById(R.id.lb_duration_2);
        dateTimeLabelInMap = findViewById(R.id.lb_date_time);
        previewImage = findViewById(R.id.preview_image);

        //FrameLayout frameLayout = findViewById(R.id.inherit_frame_summary);
        lbDistance = findViewById(R.id.lb_distance);
        lbTime = findViewById(R.id.lb_time);
        inputPaceDisplayTime = findViewById(R.id.lb_step);
        lbCalories = findViewById(R.id.lb_calories);
        btnSubmit = findViewById(R.id.btn_submit_result);

    }

    private void viewEventListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSocialShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReviewEventPage();
            }
        });

    }

    private void updateProps() {
        if (workoutInfo != null) {
            lbDistance.setText(Globals.DCM_2.format(workoutInfo.getDistance()));
            lbTime.setText(workoutInfo.getTime_string());
            inputPaceDisplayTime.setText(DateTimeUtils.toTimeFormat((long) ((workoutInfo.getPace() * 60) * 1000)));
            lbCalories.setText(Globals.DCM.format(workoutInfo.getCalory()));
            if (workoutInfo.isIs_sync()) {
                btnSubmit.setVisibility(View.GONE);
            } else {
                btnSubmit.setVisibility(View.VISIBLE);
            }

            distanceInMapLabel.setText(Globals.DCM_2.format(workoutInfo.getDistance()));
            durationInMapLabel.setText(workoutInfo.getTime_string());
            dateTimeLabelInMap.setText(workoutInfo.getWorkoutDate());
        }
    }

    /**
     * Initial GoogleMap
     */
    private void initMap() {
        // prepare usage variables
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        // transaction
        fm.beginTransaction()
                .replace(frameMap.getId(), mapFragment)
                .commit();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

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
    }

    private void shouldRedrawPolyline() {
        // prepare usage variables
        final String mtn = ct + "shouldRedrawPolyline() ";
        // should display summary record
        try {
            if (workoutInfo.getLocations() != null && workoutInfo.getLocations().size() > 0) {
                // update map camera
                LatLng ll = new LatLng(workoutInfo.getLocations().get(0).getLatitude(), workoutInfo.getLocations().get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));

                if (mMapUtils != null) {
                    // update props
                    mMapUtils.points = workoutInfo.getLatLngList();

                    // redraw polyline
                    mMapUtils.redrawPolyline();

                    // zoom to fit
                    mMapUtils.zoomToFit();
                }
            } else {
                if (mPmUtils.checkPermission(Globals.ACCESS_FINE_LOCAITON)) {
                    showLastLocation();
                } else {
                    mPmUtils.requestPermissions(Globals.RC_PERMISSION, Globals.ACCESS_FINE_LOCAITON);
                }
            }

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());

        }
    }

    private void showLastLocation() {
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);

        // prepare usage variables
        Location lkLoc = mLocUtils.getLastKnownLocation();

        // last known not found
        if (lkLoc != null) {
            // prepare usage variables
            LatLng ll = new LatLng(lkLoc.getLatitude(), lkLoc.getLongitude());
            // update map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Configs.GoogleMap.INITIAL_ZOOM));
        }
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
                        toSharePage(bitmap);

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
    private void toSharePage(Bitmap previewImage) {
        // prepare usage variables
        SharePage page = new SharePage();

        //update props
        page.setRecorderObject(workoutInfo.toRealmRecorderObject());
        page.setPreviewMapImage(previewImage);

        // to page
        //ChildFragmentUtils.newInstance(this).addChildFragment(CONTAINER_ID, page);
        mFragmentUtils.replaceFragment(page);
    }

    private void toReviewEventPage() {
        mActiveRegisteredEventCheckerPageDialog = new ActiveRegisteredEventCheckerPage();
        mActiveRegisteredEventCheckerPageDialog.show(getSupportFragmentManager(), "ReviewEvent");
    }

    @Override
    public void onConfirmEvents(List<EventIdAndPartnerObject> selectedEvents) {
        //TODO("Send distance from event id")
        // prepare usage variables
        final String mtn = ct + "onConfirmEvents() ";

        // condition
        if (mOnSubmitMultiResult) return;

        // update flag
        mOnSubmitMultiResult = true;

        // condition
        if (selectedEvents != null) {
            L.i(mtn + "going to save record.");
            this.selectedEvents = selectedEvents;
            saveMapToDeviceSubmitMultiEvents();

        }
    }

    private void saveMapToDeviceSubmitMultiEvents() {
        if (!requestPermissionsWriteStoragePermission()) return;

        // capture google map preview image
        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                //frameMap.setVisibility(View.GONE);
                previewImage.setImageBitmap(bitmap);
                previewImage.setVisibility(View.VISIBLE);

                // snap and save as file
                File file = DeviceUtils.instance(WorkoutDetailPage.this).takeScreenshot2(WorkoutDetailPage.this, R.id.frame_map);

                apiSubmitMultiEvents(selectedEvents, file);
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

    /**
     * API methods
     */
    private void apiSubmitMultiEvents(List<EventIdAndPartnerObject> selectedEvents, File fileImage) {
        // prepare usage variables
        final String mtn = ct + "apiSubmitMultiEvents() ";
        final rqSubmitActivitiesWorkout request = new rqSubmitActivitiesWorkout();

        // update props
        request.setEvent_activity(selectedEvents);
        request.setWorkout_info(workoutInfo);
        mOnSubmitMultiResult = true;
        // fire
        new SubmitActivitiesWorkoutService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);

                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    // remove fragment dialog
                    mActiveRegisteredEventCheckerPageDialog.dismissAllowingStateLoss();

                    // clear flag
                    mOnSubmitMultiResult = false;

                    btnSubmit.setVisibility(View.GONE);

                    //TODO("Refresh profile page")
                    Toast.makeText(WorkoutDetailPage.this, "ส่งผลสำเร็จ", Toast.LENGTH_SHORT).show();

                    previewImage.setImageDrawable(null);
                    previewImage.setVisibility(View.GONE);

                    //Refresh event for profile page
                    RxBus.publish(RxBus.SUBJECT, new RefreshEvent());
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // remove fragment dialog
                mActiveRegisteredEventCheckerPageDialog.dismissAllowingStateLoss();

                // clear flag
                mOnSubmitMultiResult = false;

                previewImage.setImageDrawable(null);
                previewImage.setVisibility(View.GONE);

            }
        }).doIt(request, fileImage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Globals.RC_PERMISSION) {
            if (isPermissionsGranted(permissions, grantResults)) {
                showLastLocation();
            }
        } else if (requestCode == RC_PERMISSION_SAVE_FILE) {
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

    private boolean isPermissionsGranted(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((permissions.length == 0 || grantResults.length == 0) && permissions.length != grantResults.length) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private AlertDialog alertDialog(String title, String desc, DialogInterface.OnClickListener listener) {
        // prepare usage variables
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // update props
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("ตกลง", listener);
        //builder.setNegativeButton("ยกเลิก", listener);

        // show
        return builder.create();

    }
}