package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkMultipartProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.io.File;

public class UpdateProfileImageService extends xRequest {

    private final String ct = "UpdateProfileImageService->";

    public UpdateProfileImageService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(File profileImage) {
        final String mtn = ct + "doIt() ";
        if (!TokenManager.Companion.isAlive()) {
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkMultipartProps props = new NetworkMultipartProps();

        //--> props
        props.setUrl(APIs.UPDATE_USER_PROFILE_IMAGE.VAL);
        props.addHeader(Globals.HEADER_AUTHORIZATION, TokenManager.Companion.getAccessToken());
        props.addImageFile("upload", profileImage.getPath());

        L.i(mtn + "request-body: " + Globals.GSON.toJson(props));
        //--> fire
        nw.postMultiPath(props, networkCallback);
    }
}
