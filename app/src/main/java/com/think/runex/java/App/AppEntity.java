package com.think.runex.java.App;

import com.think.runex.java.Models.RealmRecorderObject;
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Models.UserObject;

public class AppEntity {
    private final String ct = "AppEntity->";

    public boolean isLoggedIn = false;
    public TokenObject token = new TokenObject();
    public UserObject user = new UserObject();
    public RealmRecorderObject temporaryRecorder = null;
    //public List<LatLng> temporaryPoints = null;

    public AppEntity setUser( UserObject user ){
        this.user = user;
        return this;
    }

    public AppEntity setToken( TokenObject token ){
        this.token = token;
        return this;
    }
    public AppEntity setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        return this;
    }

}
