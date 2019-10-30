package com.think.runex.java.App;

import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.L;

public class AppEntity {
    private final String ct = "AppEntity->";

    public boolean isLoggedIn = false;
    public TokenObject token = new TokenObject();
    public UserObject user = new UserObject();

    public AppEntity setUser( UserObject user ){
        this.user = user;
        return this;
    }

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
