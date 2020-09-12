package com.think.runex.java.App;

import com.google.android.gms.maps.model.LatLng;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.L;

import java.util.List;

public class AppEntity {
    private final String ct = "AppEntity->";

    public boolean isLoggedIn = false;
    public TokenObject token = new TokenObject();
    public UserObject user = new UserObject();
    public RecorderObject temporaryRecorder = null;
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
