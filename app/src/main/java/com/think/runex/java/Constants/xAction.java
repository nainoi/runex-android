package com.think.runex.java.Constants;

public enum xAction {
    NONE(-1),
    ADD(0),

    SUCCESS(1001),
    FAILED( 1002);

    public final int ID;

    private xAction(int ID){
        this.ID = ID;
    }
}
