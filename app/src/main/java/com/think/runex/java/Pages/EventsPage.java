package com.think.runex.java.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Utils.L;

public class EventsPage extends xFragment {
    /** Main variables */
    private final String ct = "EventsPage->";

    // instance variables
    private App mApp;

    // explicit variables

    // views
    private View btnANL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_events, container, false);

        // instance variables
        mApp = App.instance( getActivity() );

        // Button
        btnANL = v.findViewById(R.id.btn_action_need_login);
        btnANL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prepare usage variables
                final String mtn = ct +"onClick() ";
                AppEntity appEntity = mApp.getAppEntity();

                if( !appEntity.isLoggedIn ){
                    App.instance(activity).serveLoginPage( EventsPage.this, Configs.RC_BACK_FROM_LOGIN);

                } else if( appEntity.token.isAlive() ){
                    L.i(mtn +"activity["+ activity +"]");
                    Toast.makeText(activity, "Hello Someone", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct +"onActivityResult() ";

        L.i(mtn +"back from logged in.");
    }
}
