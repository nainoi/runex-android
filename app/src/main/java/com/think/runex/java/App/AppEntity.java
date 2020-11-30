package com.think.runex.java.App;

import com.think.runex.feature.user.UserInfo;

public class AppEntity {
    private final String ct = "AppEntity->";


    public UserInfo user = new UserInfo();
    //public RealmRecorderObject temporaryRecorder = null;
    //public List<LatLng> temporaryPoints = null;

    public AppEntity setUser(UserInfo user) {
        this.user = user;
        return this;
    }
}
