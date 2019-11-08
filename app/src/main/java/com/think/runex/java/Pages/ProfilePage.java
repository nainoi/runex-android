package com.think.runex.java.Pages;

import android.app.Activity;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.L;

import java.util.Locale;

public class ProfilePage extends xFragment implements SwipeRefreshLayout.OnRefreshListener{
    /**
     * Main variables
     */
    private final String ct = "ProfilePage->";

    // instance variables
    // views
    private SwipeRefreshLayout refreshLayout;
    private ImageView profileImage;
    private TextView lbFullname, lbEmail;
    private TextView lbTotalDistance;

    /** Implement methods */
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing( false );

        App.instance(activity).clear().serveLoginPage(this, Globals.RC_NEED_LOGIN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_profile, container, false);

        // views matching
        viewsMatching(v);

        // views event listener
        refreshLayout.setOnRefreshListener( this );

        // view binding
        viewBinding(App.instance(activity).getAppEntity().user);

        return v;
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
            lbTotalDistance.setText(String.format("%,.2f", 0.00) +"");

            L.i(mtn +"avatar: "+ dbb.getAvatar());

            Picasso.get().load( dbb.getAvatar() ).into( profileImage );

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }
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
    }

    /** Life cycle */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == Globals.RC_NEED_LOGIN && resultCode == Activity.RESULT_OK ){
            // binding view
            viewBinding( App.instance(activity).getAppEntity().user );
        }
    }
}
