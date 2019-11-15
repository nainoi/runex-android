package com.think.runex.java.Models;

import java.util.List;

public class EventDetailObject {

    /**
     * code : 200
     * msg : ok
     * data : {"id":"5dc02b9c874a693a72ef612f","user_id":"5db5bbd1874a693a72ef6091","event_id":"5d93c36369a63dca15f80364","event_user":"5d93c36369a63dca15f80364.5db5bbd1874a693a72ef6091","activity_info":[{"id":"5dc02b9c874a693a72ef612e","distance":0.01,"img_url":"","caption":"","activity_date":"2019-11-04T13:46:04.662Z","created_at":"2019-11-04T13:46:04.741Z","updated_at":"2019-11-04T13:46:04.741Z"}],"total_distance":0.01}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5dc02b9c874a693a72ef612f
         * user_id : 5db5bbd1874a693a72ef6091
         * event_id : 5d93c36369a63dca15f80364
         * event_user : 5d93c36369a63dca15f80364.5db5bbd1874a693a72ef6091
         * activity_info : [{"id":"5dc02b9c874a693a72ef612e","distance":0.01,"img_url":"","caption":"","activity_date":"2019-11-04T13:46:04.662Z","created_at":"2019-11-04T13:46:04.741Z","updated_at":"2019-11-04T13:46:04.741Z"}]
         * total_distance : 0.01
         */

        private String id;
        private String user_id;
        private String event_id;
        private String event_user;
        private double total_distance;
        private List<ActivityInfoBean> activity_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getEvent_user() {
            return event_user;
        }

        public void setEvent_user(String event_user) {
            this.event_user = event_user;
        }

        public double getTotal_distance() {
            return total_distance;
        }

        public void setTotal_distance(double total_distance) {
            this.total_distance = total_distance;
        }

        public List<ActivityInfoBean> getActivity_info() {
            return activity_info;
        }

        public void setActivity_info(List<ActivityInfoBean> activity_info) {
            this.activity_info = activity_info;
        }

        public static class ActivityInfoBean {
            /**
             * id : 5dc02b9c874a693a72ef612e
             * distance : 0.01
             * img_url :
             * caption :
             * activity_date : 2019-11-04T13:46:04.662Z
             * created_at : 2019-11-04T13:46:04.741Z
             * updated_at : 2019-11-04T13:46:04.741Z
             */

            private String id;
            private double distance;
            private String img_url;
            private String caption;
            private String activity_date;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getCaption() {
                return caption;
            }

            public void setCaption(String caption) {
                this.caption = caption;
            }

            public String getActivity_date() {
                return activity_date;
            }

            public void setActivity_date(String activity_date) {
                this.activity_date = activity_date;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }
    }
}