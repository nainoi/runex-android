package com.think.runex.java.Pages;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.think.runex.BuildConfig;
import com.think.runex.R;
import com.think.runex.feature.auth.TokenManager;
import com.think.runex.feature.user.UserInfo;
import com.think.runex.java.App.App;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Models.WorkoutListObject;
import com.think.runex.java.Pages.Record.WorkoutsAdapter;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetWorkoutsService;
import com.think.runex.java.Utils.Network.Services.LogoutService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.RxBus;
import com.think.runex.java.event.RefreshEvent;
import com.think.runex.java.event.UpdateProfileEvent;
import com.think.runex.ui.MainActivity;
import com.think.runex.util.AppPreference;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

import static com.think.runex.config.ConstantsKt.KEY_ACCESS_TOKEN;

public class ProfilePage extends xFragment implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, WorkoutsAdapter.OnDeleteRecordListener {
    /**
     * Main variables
     */
    private final String ct = "ProfilePage->";

    private FragmentUtils fragmentUtils;

    // instance variables
    private AlertDialog mDialog;
    private WorkoutListObject.DataBean mRunningHist;
    private WorkoutsAdapter workoutsAdapter;

    // explicit variables
    private boolean mOnLoginHasChanged = false;
    private boolean ON_NETWORKING = false;

    // views
    private SwipeRefreshLayout refreshLayout;
    private ImageView profileImage;
    private TextView lbFullname, lbEmail, lbVersion;
    private TextView lbTotalDistance;
    private View btnSignOut;
    private RecyclerView recyclerView;
    private TextView btnEdit;

    private Consumer<Object> refreshEvent;
    private Consumer<Object> updateProfileEvent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_profile, container, false);

        // views matching
        viewsMatching(v);

        // views event listener
        viewEventListener();

        fragmentUtils = FragmentUtils.newInstance(requireActivity(), R.id.display_fragment_frame);

        // view binding
        setProfileDataToViews(App.instance(activity).getAppEntity().user);

        // recycler view props
        recyclerViewProps();

        registerRefreshEvent();
        registerUpdateProfileEvent();
        return v;
    }

    /**
     * Life cycle
     */
    @Override
    public void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct + "onResume() ";

        if (mRunningHist == null || mOnLoginHasChanged) {
            // clear flag
            mOnLoginHasChanged = false;

            // condition
            if (ON_NETWORKING) return;

            // update flag
            ON_NETWORKING = true;

            // get my profile
            apiGetWorkouts();

        } else {

            try {
                // total distance
                lbTotalDistance.setText(Globals.DCM.format(mRunningHist.getTotal_distance()) + " km");

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }

        }
    }

    /**
     * Views matching
     */
    private void viewsMatching(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        lbFullname = v.findViewById(R.id.lb_user_name);
        lbEmail = v.findViewById(R.id.lb_email);
        lbVersion = v.findViewById(R.id.lb_version);
        lbTotalDistance = v.findViewById(R.id.lb_total_running_distance);
        profileImage = v.findViewById(R.id.profile_image);
        btnEdit = v.findViewById(R.id.lb_edit);

        //--> Buttons
        btnSignOut = v.findViewById(R.id.frame_sign_out);
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnSignOut.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentUtils.replaceFragment(new EditProfilePage());
            }
        });
    }


    /**
     * Implement methods
     */
    @Override
    public xFragment onResult(xTalk xTalk) {
        if (xTalk.requestCode == Globals.RC_REFRESH && isAdded()) {
            // condition
            if (ON_NETWORKING) return null;

            // display progress dialog
            refreshLayout.setRefreshing(true);

            // request on refresh
            onRefresh();

        }

        return super.onResult(xTalk);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_sign_out:

                mDialog = dialog("ยืนยัน", "ยืนยันการออกจากระบบ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // condition
                        if (i == AlertDialog.BUTTON_POSITIVE) {
                            // update flag
                            ON_NETWORKING = true;

                            // api logout
                            apiLogout();

                        } else mDialog.dismiss();
                    }
                });

                // display
                mDialog.show();

                break;
        }
    }

    @Override
    public void onRefresh() {
        // get running history
        apiGetWorkouts();

    }


    /**
     * API methods
     */
    private void apiGetWorkouts() {
        // prepare usage variables
        final String mtn = ct + "apiGetRunningHistory() ";

        ON_NETWORKING = true;

        new GetWorkoutsService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);
                ON_NETWORKING = false;
                try {
                    // convert to running history object
                    WorkoutListObject rhis = Globals.GSON.fromJson(response.jsonString, WorkoutListObject.class);

                    // prepare usage variables
                    double totalDistance = 0.0;

                    // condition
                    if (rhis.getData().getActivity_info().size() > 0) {
                        // update props
                        totalDistance = (mRunningHist = rhis.getData()).getTotal_distance();

                        try {
                            workoutsAdapter.submitList(mRunningHist.getActivity_info(), 7);

                        } catch (Exception e) {
                            L.e(mtn + "Err: " + e.getMessage());

                        }

                    } else {
                        // update props
                        mRunningHist = null;

                        // clear record list
                        workoutsAdapter.submitList(new ArrayList<>());

                    }

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM_2.format(totalDistance) + " km");

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                // gone progress dialog
                hideProgressDialog();
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // gone progress dialog
                hideProgressDialog();

                // clear flag
                ON_NETWORKING = false;

            }
        }).doIt();
    }

    /**
     * API methods
     */
    private void apiLogout() {
        ON_NETWORKING = true;
        new LogoutService(getActivity(), new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                if (response.responseCode == HttpURLConnection.HTTP_OK || response.responseCode == HttpURLConnection.HTTP_ACCEPTED) {

                    // update flag
                    ON_NETWORKING = false;

                    // dismiss dialog
                    mDialog.dismiss();

                    // sign out and sign in
                    TokenManager.Companion.clearToken();
                    AppPreference.INSTANCE.createPreference(activity).edit().remove(KEY_ACCESS_TOKEN).apply();

                    //Start new ManActivity
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    activity.finish();
//                    App.instance(activity)
//                            .clear()
//                            .serveLoginPage(ProfilePage.this, Globals.RC_NEED_LOGIN);

                }

            }

            @Override
            public void onFailure(xResponse response) {
                // dismiss dialog
                mDialog.dismiss();

                // update flag
                ON_NETWORKING = false;

            }

        }).doIt();
    }

    /**
     * Feature methods
     */
    private AlertDialog dialog(String title, String desc, DialogInterface.OnClickListener listener) {
        // prepare usage variables
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // update props
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("ยืนยัน", listener);
        builder.setNegativeButton("ยกเลิก", listener);

        // show
        return builder.create();

    }

    private void hideProgressDialog() {
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);

    }

    private void toMainActivity() {
        Intent i = new Intent(activity, MainActivity.class);
        startActivity(i);
    }

    /**
     * Views binding
     */
    private void setProfileDataToViews(UserInfo user) {
        // prepare usage variables
        final String mtn = ct + "viewBinding() ";

        try {

            lbFullname.setText(user.getFullName());
            lbEmail.setText(user.getEmail());
//            lbTotalDistance.setText(String.format("%,.2f", 0.00) + "");

            L.i(mtn + "avatar: " + user.getAvatar());
            Picasso.get().load(user.getAvatar()).into(profileImage);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        lbVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }

    /**
     * Recycler view props
     */
    private void recyclerViewProps() {
        // prepare usage variables
        final String mtn = ct + "recyclerViewProps() ";
        workoutsAdapter = new WorkoutsAdapter(this, false, this);

        // update props
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(workoutsAdapter);

    }

    @Override
    public void onItemClicked(int position) {
        WorkoutInfo workoutInfo = workoutsAdapter.getItem(position);
        Intent intent = new Intent(getContext(), WorkoutDetailPage.class);
        intent.putExtra("workoutInfo", workoutInfo);
        startActivity(intent);
    }


    @Override
    public void onDeleteRecord(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Globals.RC_NEED_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                // update flag
                mOnLoginHasChanged = true;

                // binding view
                setProfileDataToViews(App.instance(activity).getAppEntity().user);

            } else {

                // go to boarding page
                toMainActivity();

                // exit from this page
                activity.finish();

            }
        }
    }

    private void registerRefreshEvent() {
        refreshEvent = new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof RefreshEvent) {
                    // display progress dialog
                    refreshLayout.setRefreshing(true);

                    // request on refresh
                    onRefresh();
                }
            }
        };
        //Subscribe event when setup.
        RxBus.subscribe(RxBus.SUBJECT, refreshEvent, refreshEvent);
    }

    private void unRegisterRefreshEvent() {
        if (refreshEvent != null) {
            RxBus.unregister(refreshEvent);
            refreshEvent = null;
        }
    }

    private void registerUpdateProfileEvent() {
        updateProfileEvent = new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e("jozzee", "updateProfileEvent");
                if (o instanceof UpdateProfileEvent) {
                    Log.e("jozzee", "updateProfileEvent true");
                    UpdateProfileEvent event = (UpdateProfileEvent) o;
                    setProfileDataToViews(event.getUserInfo());
                }
            }
        };
        //Subscribe event when setup.
        RxBus.subscribe(RxBus.SUBJECT, updateProfileEvent, updateProfileEvent);
    }

    private void unRegisterUpdateProfileEvent() {
        if (updateProfileEvent != null) {
            RxBus.unregister(updateProfileEvent);
            updateProfileEvent = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterRefreshEvent();
        unRegisterUpdateProfileEvent();
    }
}
