package com.think.runex.java.Utils.Network.Request;

import android.app.Activity;

import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class SubmitRunningResultService extends xRequest {
    public SubmitRunningResultService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(NetworkProps props){
        // prepare usage variables
        NetworkUtils nu = NetworkUtils.newInstance( activity );
        nu.postFormData( props, networkCallback );
    }
}
