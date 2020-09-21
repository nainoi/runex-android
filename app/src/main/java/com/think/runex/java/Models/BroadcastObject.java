package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.think.runex.java.Constants.BroadcastAction;
import com.think.runex.java.Constants.BroadcastType;

public class BroadcastObject implements Parcelable {

    public static final Creator<BroadcastObject> CREATOR = new Creator<BroadcastObject>() {
        @Override
        public BroadcastObject createFromParcel(Parcel in) {
            return new BroadcastObject(in);
        }

        @Override
        public BroadcastObject[] newArray(int size) {
            return new BroadcastObject[size];
        }
    };

    public BroadcastType broadcastType = null;
    public BroadcastAction broadcastAction = null;
    public RealmRecorderObject recorderObject = null;
    public DebugUIObject debugUIObject = null;
    public BackgroundServiceInfoObject serviceInfoObject = null;

    public BroadcastObject() {
    }

    protected BroadcastObject(Parcel in) {
        String type = in.readString();
        if (type != null && !TextUtils.isEmpty(type)) {
            broadcastType = BroadcastType.valueOf(type);
        }
        String action = in.readString();
        if (action != null && !TextUtils.isEmpty(action)) {
            broadcastAction = BroadcastAction.valueOf(action);
        }
        recorderObject = in.readParcelable(RealmRecorderObject.class.getClassLoader());
        debugUIObject = in.readParcelable(DebugUIObject.class.getClassLoader());
        serviceInfoObject = in.readParcelable(BackgroundServiceInfoObject.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        if (broadcastType != null) {
            parcel.writeString(broadcastType.name());
        } else {
            parcel.writeString("");
        }

        if (broadcastAction != null) {
            parcel.writeString(broadcastAction.name());
        } else {
            parcel.writeString("");
        }
        parcel.writeParcelable(recorderObject, flags);
        parcel.writeParcelable(debugUIObject, flags);
        parcel.writeParcelable(serviceInfoObject, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
