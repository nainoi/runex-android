package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.feature.auth.data.TokenManager;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class LogoutService extends xRequest {
    /** Main variables */
    private final String ct = "LogoutService->";

    public LogoutService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(){
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

        NetworkProps props = new NetworkProps();
        //--> update props
        props.setUrl(APIs.LOGOUT.VAL);
        //--> headers
        props.addHeader("Authorization", TokenManager.Companion.getAccessToken());

        // request
        nu.postJSON(props, networkCallback);
    }
}
