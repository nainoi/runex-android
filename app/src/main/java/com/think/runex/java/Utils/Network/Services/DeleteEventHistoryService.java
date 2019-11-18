package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class DeleteEventHistoryService extends xRequest {
    public DeleteEventHistoryService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(String eventId, String historyId){
        if (!appEntity.token.isAlive()) {
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkProps props = new NetworkProps();

        //--> props
        props.setUrl(String.format(APIs.DELETE_EVENT_HISTORY.VAL, eventId, historyId));
        props.addHeader(Globals.HEADER_AUTHORIZATION, Globals.TOKEN_TYPE + " " + appEntity.token.getToken());

        //--> fire
        nw.delete(props, networkCallback);
    }
}
