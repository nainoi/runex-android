package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;
import android.util.Log;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class MyRegEventService extends xRequest {
    private final String ct = "MyRegEventService->";

    public MyRegEventService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt() {
        // validate token
        if (!appEntity.token.isAlive()) {
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
        props.setUrl(APIs.GET_REGISTERED_EVENT.VAL);
        //--> headers
        props.addHeader("Authorization", "Bearer "+ appEntity.token.getToken());

        // request
        nu.get(props, networkCallback);
    }
}