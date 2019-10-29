package com.think.runex.java.Utils.Network;

import com.think.runex.java.Utils.Network.Response.xResponse;

public interface onNetworkCallback {
    void onSuccess(xResponse response);
    void onFailure(xResponse response);
}
