package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;

import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkMultipartProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class AddEventActivityService extends xRequest {
    // prepare usage variables
    private final String ct = "AddEventActivityService->";

    public AddEventActivityService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(String eventId, String distance, String date, String imagePath, String activityType) {
        // prepare usage variables
        final String mtn = ct + "doIt() ";

        if (!appEntity.token.isAlive()) {
            // callback
            networkCallback.onSuccess(unauthorized());

            // exit from this process
            return;
        }

        // prepare usage variables
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkMultipartProps props = new NetworkMultipartProps();

        //--> update props
        props.setUrl(APIs.SUBMIT_RUNNING_RESULT.VAL);
        if (imagePath != null && imagePath.length() > 0) {
            props.addImageFile("image", imagePath);
        }
        props.addFormDataPart("distance", distance);
        props.addFormDataPart("activity_date", date);
        props.addFormDataPart("event_id", eventId);
        props.addFormDataPart("activity_type", activityType);

        //--> headers
        props.addHeader("Authorization", "Bearer " + appEntity.token.getToken());

        L.i(mtn + "Bearer " + appEntity.token.getToken());

        // request
        nw.postMultiPath(props, networkCallback);
    }
}
