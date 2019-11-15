package com.think.runex.java.Constants;

public enum Priority {
    NONE(-1),
    PARENT(0),
    CHILD(1),
    ;

    public final int ID;
    private Priority( int priorityId){
        this.ID = priorityId;
    }
}
