package com.think.runex.java.Models;

public class GPSFileRecordObject {
    public xGPSRecord record;
    public RealmRecorderObject recorder;

    public static class xGPSRecord {
        public long timestamp;
        public int type;
        public double latitude;
        public double longitude;
    }
}
