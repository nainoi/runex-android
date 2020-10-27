package com.think.runex.java.Pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.L;

public class EventsPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "EventsPage->";

    // instance variables
    private App mApp;

    // explicit variables

    // views
    private TextView btnANL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_events, container, false);

        // instance variables
        mApp = App.instance(getActivity());

        // Button
        btnANL = v.findViewById(R.id.btn_action_need_login);
        btnANL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prepare usage variables
                final String mtn = ct + "onClick() ";
                AppEntity appEntity = mApp.getAppEntity();

                if (!TokenManager.Companion.isAlive()) {
                    mApp.serveLoginPage(EventsPage.this, Globals.RC_NEED_LOGIN);

                } else if (TokenManager.Companion.isAlive()) {
                    Toast.makeText(activity, "Hello " + appEntity.user.getData().getFullname(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding();

        return v;
    }

    /**
     * Feature methods
     */
    private void binding() {
        // prepare usage variables
        UserObject user = mApp.getAppEntity().user;

        // update
        btnANL.setText(user.getData().getFirstname() + " " + btnANL.getText());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";

        // login successfully
        if (requestCode == Globals.RC_NEED_LOGIN && resultCode == Activity.RESULT_OK) {
            // binding
            binding();

        }

        L.i(mtn + "back from logged in.");
    }
}
