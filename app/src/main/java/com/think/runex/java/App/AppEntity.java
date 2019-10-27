package com.think.runex.java.App;

public class AppEntity {
    public boolean isLoggedIn = false;

    public AppEntity setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        return this;
    }
}
