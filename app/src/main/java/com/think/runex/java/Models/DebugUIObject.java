package com.think.runex.java.Models;

import com.think.runex.java.Utils.GoogleMap.xLocation;

import java.io.Serializable;

public class DebugUIObject implements Serializable {
    public xLocation xLocation;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;

}
