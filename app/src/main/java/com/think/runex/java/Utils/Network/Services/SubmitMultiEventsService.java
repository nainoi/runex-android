package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqSocialPd;
import com.think.runex.java.Utils.Network.Request.rqSubmitMultiEvents;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class SubmitMultiEventsService extends xRequest {
    /** Main variables */
    private final String ct = "SubmitMultiEventsService->";

    public SubmitMultiEventsService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqSubmitMultiEvents request){
        // prepare usage variables
        final String mtn = ct +"doIt() ";
        final NetworkUtils nw = NetworkUtils.newInstance( activity );
        final NetworkProps props = new NetworkProps();

        //--> props
        props.setUrl( APIs.SUBMIT_MULTI_EVENTS.VAL);
        //--> headers
        props.addHeader("Authorization", "Bearer "+ appEntity.token.getToken());
        props.addHeader("Content-Type", "application/json");
        props.addMultiParts("calory", request.calory +"");
        props.addMultiParts("distance", request.distance +"");
        props.addMultiParts("time", request.time +"");
        props.addMultiParts("caption", request.caption +"");

        for(int a = 0; a < request.event_id.length; a++){
            props.addMultiParts("event_id", request.event_id[a]);

        }

        L.i(mtn +"request-body: "+ Globals.GSON.toJson( request ));

        //--> fire
        nw.postFormData(props, networkCallback);

    }
    public String toString(String[] arrayData) {
        // prepare usage variables
        StringBuilder stringBuilder = new StringBuilder();

        if( arrayData == null ) return "";

        stringBuilder.append("[");

        // get event id
        for (int i = 0 ; i < arrayData.length; i++) {
            // string concat
            stringBuilder.append(arrayData[i]);

            // merge symbol
            if (i < arrayData.length - 1) {
                stringBuilder.append(",");

            }
        }
        stringBuilder.append("]");


        return stringBuilder.toString();
    }
}
