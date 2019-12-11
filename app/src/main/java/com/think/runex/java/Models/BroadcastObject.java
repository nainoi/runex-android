package com.think.runex.java.Models;

import com.think.runex.java.Constants.BroadcastAction;
import com.think.runex.java.Constants.BroadcastType;
import com.think.runex.java.Utils.GoogleMap.xLocation;

import java.io.Serializable;

public class BroadcastObject implements Serializable {
    public BroadcastType broadcastType = null;
//    public RecorderObject recorderObject = null;
    public Object attachedObject = null;
    public BroadcastAction broadcastAction = null;

}
