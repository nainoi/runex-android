package com.think.runex.java.Utils.Network.Services;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.FileUtils;

import com.think.runex.feature.auth.TokenManager;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkMultipartProps;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqSubmitActivitiesWorkout;
import com.think.runex.java.Utils.Network.Request.rqSubmitMultiEvents;
import com.think.runex.java.Utils.Network.Request.xRequest;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.io.File;
import java.nio.file.Files;

public class SubmitActivitiesWorkoutService extends xRequest {
    /**
     * Main variables
     */
    private final String ct = "SubmitActivitiesWorkoutService->";

    public SubmitActivitiesWorkoutService(Activity activity, onNetworkCallback networkCallback) {
        super(activity, networkCallback);
    }

    public void doIt(rqSubmitActivitiesWorkout request, File fileImage) {
        // prepare usage variables
        final String mtn = ct + "doIt() ";
        final NetworkUtils nw = NetworkUtils.newInstance(activity);
        final NetworkMultipartProps props = new NetworkMultipartProps();

        //--> props
        props.setUrl(APIs.SUBMIT_ACTIVITIES_WORKOUT.VAL);
        //--> headers
        props.addHeader(Globals.HEADER_AUTHORIZATION, TokenManager.Companion.getAccessToken());
        props.addHeader("Content-Type", "application/json");
        props.addImageFile("image", fileImage.getPath());
        props.addFormDataPart("event_activity",Globals.GSON.toJson(request.getEvent_activity()));
        props.addFormDataPart("workout_info",Globals.GSON.toJson(request.getWorkout_info()));

        L.i(mtn + "request-body: " + Globals.GSON.toJson(props));

        //--> fire
        nw.postMultiPath(props, networkCallback);
    }
}
