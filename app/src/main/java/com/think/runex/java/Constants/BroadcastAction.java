package com.think.runex.java.Constants;

public enum BroadcastAction {
    NONE("None" ),
    INITIAL("Start" ),
    ADD("Add" ),
    PAUSE("Pause" ),
    RESUME("Resume"),
    RESET("Reset"),
    GET_BACKGROUND_SERVICE_INFO("Get Background Service Info"),

    ;

    public final String TYPE;
    private BroadcastAction(String actionType ){
        this.TYPE = actionType;
    }
}
