package com.think.runex.java.App;

import com.think.runex.java.Models.TokenObject;

public class AppEntity {
    public boolean isLoggedIn = false;
    public TokenObject token = new TokenObject();

    public AppEntity setToken( TokenObject token ){
        this.token = token;
        return this;
    }
    public AppEntity setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        return this;
    }
}
