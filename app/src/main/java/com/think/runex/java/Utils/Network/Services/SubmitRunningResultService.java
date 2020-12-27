package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqSubmitRunningResult;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.think.runex.config.ConstantsKt.SERVER_DATE_TIME_FORMAT;

public class SubmitRunningResultService extends xRequest {
    private final String ct = "SubmitRunningResultService->";

    public SubmitRunningResultService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqSubmitRunningResult request) {
        // validate token
        if (!TokenManager.Companion.isAlive()) {
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final String mtn = ct + "doIt() ";
        NetworkUtils nu = NetworkUtils.newInstance(activity);
        String submitDate = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).format(request.activity_date);
        NetworkProps props = new NetworkProps();

        //--> update props
        props.setUrl(APIs.SUBMIT_RUNNING_RESULT.VAL);
        props.addMultiParts("distance", request.distance + "");
        props.addMultiParts("activity_date", submitDate);
        props.addMultiParts("activity_type", request.activity_type);
        props.addMultiParts("event_id", request.event_id);
        //--> headers
        props.addHeader("Authorization", TokenManager.Companion.getAccessToken());

        L.i(mtn + "Bearer " + TokenManager.Companion.getAccessToken());

        // request 
        nu.postFormData(props, networkCallback);
    }
}
