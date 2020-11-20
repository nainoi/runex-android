package com.think.runex.java.Models;

import com.think.runex.java.App.Configs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.think.runex.util.ConstantsKt.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH;

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

    public class DataBean {
        /**
         {
         "code": 200,
         "msg": "success",
         "data": {
            "id": "5fa969cb455f4732c8fee950",
            "event_id": "5f9a819af664332c7dff7b91",
            "activities": {
                "user_id": "5f7c1dc4409bdbed73f10b09",
                "total_distance": 3.49270231555856,
                "activity_info": [
                    {
                        "id": "5fa969cb455f4732c8fee94f",
                        "distance": 3.49270231555856,
                        "img_url": "",
                        "caption": "",
                        "app": "RUNEX",
                        "time": 21,
                        "activity_date": "2020-11-09T21:52:27Z",
                        "created_at": "2020-11-09T16:09:47.123Z",
                        "updated_at": "2020-11-09T16:09:47.123Z"
                    }
                ]
            }
         }
         }
         */

        private String id;
        private String event_id;
        private activities activities;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public EventDetailObject.activities getActivities() {
            return activities;
        }

        public void setActivities(EventDetailObject.activities activities) {
            this.activities = activities;
        }
    }

    public class activities {

        private String user_id;
        private double total_distance;
        private List<activity_info>activity_info;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public double getTotal_distance() {
            return total_distance;
        }

        public void setTotal_distance(double total_distance) {
            this.total_distance = total_distance;
        }

        public List<EventDetailObject.activity_info> getActivity_info() {
            return activity_info;
        }

        public void setActivity_info(List<EventDetailObject.activity_info> activity_info) {
            this.activity_info = activity_info;
        }
    }

    public class activity_info {
        private String id;
        private double distance;
        private String img_url;
        private String caption;
        private String app;
        private int time;
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

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
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

        public String ActivityDate() {
            if (activity_date == null) {
                return "";
            }
            String changedDateTime = activity_date;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Configs.SERVER_DATE_TIME_FORMAT, Locale.getDefault());
                Date date = sdf.parse(activity_date);
                if (date != null) {
                    changedDateTime = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH, Locale.getDefault()).format(date);
                }
            } catch (Throwable error) {
                try {
                    error.printStackTrace();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault());
                    Date date = null;

                    date = sdf.parse(activity_date);

                    if (date != null) {
                        changedDateTime = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH, Locale.getDefault()).format(date);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return changedDateTime;

        }
    }
}
