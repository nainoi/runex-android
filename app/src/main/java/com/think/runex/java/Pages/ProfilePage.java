package com.think.runex.java.Pages;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.application.MainActivity;
import com.think.runex.java.App.App;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RunningHistoryObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetRunningHistory;
import com.think.runex.java.Utils.Network.Services.LogoutService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;

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

    // explicit variables
    private boolean mOnLoginHasChanged = false;
    private boolean ON_NETWORKING = false;

    // views
    private SwipeRefreshLayout refreshLayout;
    private ImageView profileImage;
    private TextView lbFullname, lbEmail;
    private TextView lbTotalDistance;
    private View btnSignOut;

    /**
     * Implement methods
     */
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
                    if (rhis.getData().size() > 0) totalDistance = (mRunningHist = rhis.getData().get(0)).getTotal_distance();
                    else mRunningHist = null;

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM.format(totalDistance) + " km");

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                // hide progress dialog
                hideProgressDialog();
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // hide progress dialog
                hideProgressDialog();

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
                if (response.responseCode == HttpURLConnection.HTTP_OK) {

                    // update flag
                    ON_NETWORKING = false;

                    // dismiss dialog
                    mDialog.dismiss();

                    // sign out and sign in
                    App.instance(activity)
                            .clear()
                            .serveLoginPage(ProfilePage.this, Globals.RC_NEED_LOGIN);


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

        if (mOnLoginHasChanged) {
            // clear flag
            mOnLoginHasChanged = false;

            // get my profile
            apiGetRunningHistory();

        } else {
            try {
                // total distance
                lbTotalDistance.setText(Globals.DCM.format(mRunningHist.getTotal_distance()) +" km");

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }

        }
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
