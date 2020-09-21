package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.think.runex.java.Utils.GoogleMap.xLocation;


public class DebugUIObject implements Parcelable {

    public static final Creator<DebugUIObject> CREATOR = new Creator<DebugUIObject>() {
        @Override
        public DebugUIObject createFromParcel(Parcel in) {
            return new DebugUIObject(in);
        }

        @Override
        public DebugUIObject[] newArray(int size) {
            return new DebugUIObject[size];
        }
    };

    public xLocation xLocation;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;

    public DebugUIObject() {
    }

    protected DebugUIObject(Parcel in) {
        xLocation = in.readParcelable(com.think.runex.java.Utils.GoogleMap.xLocation.class.getClassLoader());
        isGPSEnabled = in.readByte() != 0;
        isNetworkEnabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(xLocation, flags);
        dest.writeByte((byte) (isGPSEnabled ? 1 : 0));
        dest.writeByte((byte) (isNetworkEnabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
