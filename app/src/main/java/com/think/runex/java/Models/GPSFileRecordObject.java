package com.think.runex.java.Models;

public class GPSFileRecordObject {
    public xGPSRecord record;
    public RecorderObject recorder;

    public static class xGPSRecord {
        public long timestamp;
        public int type;
        public double latitude;
        public double longitude;
    }
}
