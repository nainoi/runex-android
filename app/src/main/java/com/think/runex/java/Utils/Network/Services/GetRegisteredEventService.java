package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class GetRegisteredEventService extends xRequest {
    /** Main variables */
    private final String ct = "GetRegisteredEventService->";

    public GetRegisteredEventService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);

    }

    public void doIt( ){
        if( !appEntity.token.isAlive() ){
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final String mtn = ct +"apiGetEvents() ";
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkProps props = new NetworkProps();

        // update props
        props.addHeader("Authorization", Globals.TOKEN_TYPE +" "+ appEntity.token.getToken());
        props.setUrl(APIs.GET_REGISTERED_EVENT.VAL);

        // call api
        nw.get(props, networkCallback);
    }
}
