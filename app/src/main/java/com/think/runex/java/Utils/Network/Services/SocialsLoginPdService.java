package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqSocialPd;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class SocialsLoginPdService extends xRequest {
    /** Main variables */
    private final String ct = "SocialsLoginPdService->";

    public SocialsLoginPdService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqSocialPd request){
        // prepare usage variables
        final String mtn = ct +"doIt() ";
        final NetworkUtils nw = NetworkUtils.newInstance( activity );
        final NetworkProps props = new NetworkProps();

        //--> props
        props.setUrl( APIs.SOCIALS_LOGIN.VAL);
        props.addHeader("Content-Type", "application/json");
        props.addHeader("Cache-Control", "no-cache");
        props.setJsonAsObject( request );

        L.i(mtn +"request-body: "+ Globals.GSON.toJson( request ));

        //--> fire
        nw.postJSON(props, networkCallback);

    }
}
