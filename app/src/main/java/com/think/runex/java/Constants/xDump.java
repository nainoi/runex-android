package com.think.runex.java.Constants;

import java.util.List;

public class xDump {

    private List<XBean> x;

    public List<XBean> getX() {
        return x;
    }

    public void setX(List<XBean> x) {
        this.x = x;
    }

    public static class XBean {
        /**
         * latitude : 16.4284685
         * longitude : 102.840906
         */

        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
