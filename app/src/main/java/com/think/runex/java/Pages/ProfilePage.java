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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.App.App;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.RunningHistoryObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Pages.Record.RecordAdapter;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetRunningHistory;
import com.think.runex.java.Utils.Network.Services.LogoutService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.ui.MainActivity;
import com.think.runex.util.AppPreference;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static com.think.runex.util.ConstantsKt.KEY_ACCESS_TOKEN;

public class ProfilePage extends xFragment implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    /**
     * Main variables
     */
    private final String ct = "ProfilePage->";

    // instance variables
    private AlertDialog mDialog;
    private RunningHistoryObject.DataBean mRunningHist;
    private RecordAdapter recordAdapter;

    // explicit variables
    private boolean mOnLoginHasChanged = false;
    private boolean ON_NETWORKING = false;

    // views
    private SwipeRefreshLayout refreshLayout;
    private ImageView profileImage;
    private TextView lbFullname, lbEmail;
    private TextView lbTotalDistance;
    private View btnSignOut;
    private RecyclerView recyclerView;

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
        apiGetRunningHistory();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_profile, container, false);

        // views matching
        viewsMatching(v);

        // views event listener
        viewEventListener();

        // view binding
        viewBinding(App.instance(activity).getAppEntity().user);

        // recycler view props
        recyclerViewProps();

        return v;
    }

    /**
     * API methods
     */
    private void apiGetRunningHistory() {
        // prepare usage variables
        final String mtn = ct + "apiGetRunningHistory() ";

        new GetRunningHistory(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);

                try {
                    // convert to running history object
                    RunningHistoryObject rhis = Globals.GSON.fromJson(response.jsonString, RunningHistoryObject.class);

                    // prepare usage variables
                    double totalDistance = 0.0;

                    // condition
                    if (rhis.getData().size() > 0) {
                        // update props
                        totalDistance = (mRunningHist = rhis.getData().get(0)).getTotal_distance();

                        try {
                            recordAdapter.submitList(mRunningHist.getActivity_info(), 7);

                        } catch (Exception e) {
                            L.e(mtn + "Err: " + e.getMessage());

                        }

                    } else {
                        // update props
                        mRunningHist = null;

                        // clear record list
                        recordAdapter.submitList(new ArrayList<>());

                    }

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM_2.format(totalDistance) + " km");

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                // gone progress dialog
                hideProgressDialog();

                // clear flag
                ON_NETWORKING = false;
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
    private void viewBinding(UserObject user) {
        // prepare usage variables
        final String mtn = ct + "viewBinding() ";

        try {
            UserObject.DataBean dbb = user.getData();

            lbFullname.setText(dbb.getFullname());
            lbEmail.setText(dbb.getEmail());
//            lbTotalDistance.setText(String.format("%,.2f", 0.00) + "");

            L.i(mtn + "avatar: " + dbb.getAvatar());

            Picasso.get().load(dbb.getAvatar()).into(profileImage);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }
    }

    /**
     * Recycler view props
     */
    private void recyclerViewProps() {
        // prepare usage variables
        final String mtn = ct + "recyclerViewProps() ";
        recordAdapter = new RecordAdapter(false, null);

        // update props
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(recordAdapter);

    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnSignOut.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * Views matching
     */
    private void viewsMatching(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        lbFullname = v.findViewById(R.id.lb_user_name);
        lbEmail = v.findViewById(R.id.lb_email);
        lbTotalDistance = v.findViewById(R.id.lb_total_running_distance);
        profileImage = v.findViewById(R.id.profile_image);

        //--> Buttons
        btnSignOut = v.findViewById(R.id.frame_sign_out);
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
            apiGetRunningHistory();

        } else {

            try {
                // total distance
                lbTotalDistance.setText(Globals.DCM.format(mRunningHist.getTotal_distance()) + " km");

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Globals.RC_NEED_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                // update flag
                mOnLoginHasChanged = true;

                // binding view
                viewBinding(App.instance(activity).getAppEntity().user);

            } else {

                // go to boarding page
                toMainActivity();

                // exit from this page
                activity.finish();

            }
        }
    }
}
