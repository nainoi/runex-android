package com.think.runex.java.Utils.Network;

public interface onNetworkCallback {
    void onSuccess(String jsonString);
    void onFailure(Exception jsonString);
}
