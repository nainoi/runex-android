package com.think.runex.java.Constants;

public enum BroadcastAction {
    NONE("None" ),
    INITIAL("Start" ),
    ADD("Add" ),
    PAUSE("Pause" ),
    RESUME("Resume"),

    ;

    public final String TYPE;
    private BroadcastAction(String actionType ){
        this.TYPE = actionType;
    }
}
