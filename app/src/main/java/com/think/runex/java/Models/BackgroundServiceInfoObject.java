package com.think.runex.java.Models;

import java.io.Serializable;

public class BackgroundServiceInfoObject implements Serializable {
    public boolean isRecordPaused = false;
    public boolean isRecordStarted = false;
    public Object attachedObject = null;

}
