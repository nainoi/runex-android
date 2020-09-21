package com.think.runex.java.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BackgroundServiceInfoObject implements Parcelable {

    public static final Creator<BackgroundServiceInfoObject> CREATOR = new Creator<BackgroundServiceInfoObject>() {
        @Override
        public BackgroundServiceInfoObject createFromParcel(Parcel in) {
            return new BackgroundServiceInfoObject(in);
        }

        @Override
        public BackgroundServiceInfoObject[] newArray(int size) {
            return new BackgroundServiceInfoObject[size];
        }
    };

    public boolean isRecordPaused = false;
    public boolean isRecordStarted = false;
    public RealmRecorderObject recorderObject = null;
    public DebugUIObject debugUIObject = null;

    public BackgroundServiceInfoObject() {

    }

    protected BackgroundServiceInfoObject(Parcel in) {
        isRecordPaused = in.readByte() != 0;
        isRecordStarted = in.readByte() != 0;
        recorderObject = in.readParcelable(RealmRecorderObject.class.getClassLoader());
        debugUIObject = in.readParcelable(DebugUIObject.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRecordPaused ? 1 : 0));
        dest.writeByte((byte) (isRecordStarted ? 1 : 0));
        dest.writeParcelable(recorderObject, flags);
        dest.writeParcelable(debugUIObject, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
