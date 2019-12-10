package com.think.runex.java.Constants;

public enum BroadcastType {
    RECORDING("RECORDING"),
    LOCATION("LOCATION"),
    ACTIONS("ACTIONS"),
    ;

    public final String TYPE;
    private BroadcastType(String type){
        this.TYPE = type;
    }
}
