package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqSubmitRunningResult;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class SubmitRunningResultService extends xRequest {
    private final String ct = "SubmitRunningResultService->";

    public SubmitRunningResultService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqSubmitRunningResult request){
        // validate token
        if( !appEntity.token.isAlive() ){
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final String mtn = ct +"doIt() ";
        NetworkUtils nu = NetworkUtils.newInstance( activity );
        String submitDate = Globals.SDF.format( request.activity_date ).toString();
        NetworkProps props = new NetworkProps();

        //--> update props
        props.setUrl( APIs.SUBMIT_RUNNING_RESULT.VAL );
        props.addMultiParts("distance", request.distance +"");
        props.addMultiParts("activity_date", submitDate);
        props.addMultiParts("activity_type", request.activity_type);
        props.addMultiParts("event_id", request.event_id);
        //--> headers
        props.addHeader("Authorization", "Bearer "+ appEntity.token.getToken());

        L.i(mtn +"Bearer "+ appEntity.token.getToken());

        // request 
        nu.postFormData( props, networkCallback );
    }
}
