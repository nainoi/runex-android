package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqRegisterUser;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class RegisterUserService extends xRequest {
    /** Main variables */
    private final String ct = "RegisterUserService->";

    public RegisterUserService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqRegisterUser rq){
        // prepare usage variables
        final String mtn = ct + "doIt() ";
        NetworkUtils nu = NetworkUtils.newInstance(activity);
        NetworkProps props = new NetworkProps();

        //--> update props
        props.setUrl(APIs.REGISTER_USER.VAL);
        props.setJsonAsObject( rq );

        // request
        nu.postJSON(props, networkCallback);
    }
}
