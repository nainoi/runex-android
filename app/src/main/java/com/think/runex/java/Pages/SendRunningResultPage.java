package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.Request.SubmitRunningResultService;
import com.think.runex.java.Utils.Network.Request.rqSubmitRunningResult;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendRunningResultPage extends xFragment
    implements onNetworkCallback
    , View.OnClickListener {
    /** Main variables */
    private final String ct = "SendRunningResultPage->";

    // views
    private View btnSubmit
            , btnCancel;

    /** Implement methods */
    @Override
    public void onClick(View view) {
        switch( view.getId() ){
            case R.id.btn_submit: apiSubmitRunningResult(); break;
            case R.id.btn_cancel: break;
        }
    }
    @Override
    public void onSuccess(xResponse response) {
        // prepare usage variables
        final String mtn = ct +"onSuccess() ";
        L.i(mtn +"response: "+ response.jsonString);
    }

    @Override
    public void onFailure(xResponse response) {
        // prepare usage variables
        final String mtn = ct +"onFailure() ";
        L.e(mtn +"response: "+ response.jsonString);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_send_running_result, container, false);


        // view matching
        viewMatching(v);

        // view event listener
        viewEventListener();

        return v;
    }

    /** API methods */
    private void apiSubmitRunningResult(){
        // prepare usage variables
        rqSubmitRunningResult request = new rqSubmitRunningResult();

        //--> update props
        request.distance = 0.1;
        request.event_id = Globals.EVENT_ID;
        request.activity_date = System.currentTimeMillis();

        // submit running result
        new SubmitRunningResultService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                // prepare usage variables
                final String mtn = ct +"onSuccess() ";

                L.i(mtn +"json-string: "+ response.jsonString);
            }

            @Override
            public void onFailure(xResponse response) {
                // prepare usage variables
                final String mtn = ct +"onFailure() ";
                L.i(mtn +"json-string: "+ response.jsonString);

            }
        }).doIt( request );
    }

    /** View event listener */
    private void viewEventListener(){
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /** View matching */
    private void viewMatching(View v){

        btnSubmit = v.findViewById(R.id.btn_submit);
        btnCancel = v.findViewById(R.id.btn_cancel);

    }
}
