package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.feature.auth.TokenManager;
import com.think.runex.feature.user.UserInfo;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class UpdateProfileService extends xRequest {

    private final String ct = "UpdateProfileService->";

    public UpdateProfileService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(UserInfo userInfo) {
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
        props.setUrl(APIs.UPDATE_USER_PROFILE.VAL);
        props.setJsonAsObject(userInfo);
        props.addHeader(Globals.HEADER_AUTHORIZATION, TokenManager.Companion.getAccessToken());

        //--> fire
        nw.putJSON(props, networkCallback);
    }
}
