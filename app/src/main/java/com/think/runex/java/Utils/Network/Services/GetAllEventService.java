package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class GetAllEventService extends xRequest {
    /**
     * Main variables
     */
    private final String ct = "GetAllEventService->";


    public GetAllEventService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt() {
        final String mtn = ct + "doIt() ";
        if (!TokenManager.Companion.isAlive()) {
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkProps props = new NetworkProps();

        //--> props
        props.setUrl(APIs.GET_ALL_EVENT.VAL);
        props.addHeader(Globals.HEADER_AUTHORIZATION, TokenManager.Companion.getAccessToken());

        //--> fire
        nw.get(props, networkCallback);
    }
}
