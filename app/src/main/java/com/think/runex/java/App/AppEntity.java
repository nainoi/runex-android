package com.think.runex.java.App;

import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Utils.L;

public class AppEntity {
    private final String ct = "AppEntity->";

    public boolean isLoggedIn = false;
    public TokenObject token = new TokenObject();

    public AppEntity setToken( TokenObject token ){
        token.init();
        this.token = token;
        return this;
    }
    public AppEntity setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        return this;
    }

}
