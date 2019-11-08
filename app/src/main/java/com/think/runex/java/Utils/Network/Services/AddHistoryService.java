package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqAddRunningHistory;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class AddHistoryService extends xRequest {
    // prepare usage variables
    private final String ct = "AddHistoryService->";

    public AddHistoryService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqAddRunningHistory request){
        // prepare usage variables
        final String mtn = ct +"doIt() ";

        if( !appEntity.token.isAlive() ){
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final NetworkUtils nw = NetworkUtils.newInstance( activity );
        final NetworkProps props = new NetworkProps();

        //--> props
        props.setUrl( APIs.ADD_HISTORY.VAL);
        props.setJsonAsObject( request );
        props.addHeader(Globals.HEADER_AUTHORIZATION, Globals.TOKEN_TYPE +" "+ appEntity.token.getToken());

        //--> fire
        nw.postJSON(props, networkCallback);
    }
}
