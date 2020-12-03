package com.think.runex.java.event;

import com.think.runex.feature.user.UserInfo;

public class UpdateProfileEvent {

    private UserInfo userInfo;

    public UpdateProfileEvent() {

    }

    public UpdateProfileEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
