package com.think.runex.java.Utils.Network.Request;

import android.app.Activity;

import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;

abstract public class xRequest {
    protected Activity activity;
    protected AppEntity appEntity;
    protected onNetworkCallback networkCallback;

    public xRequest(Activity activity, onNetworkCallback networkCallback){
        this.activity = activity;
        appEntity = App.instance(activity).getAppEntity();
        this.networkCallback = networkCallback;

    }

    public xResponse unauthorized(){
        return new xResponse().setMessage("manual unauthorized.").setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED);

    }
}
