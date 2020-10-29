package com.think.runex.java.Models;

import java.util.ArrayList;
import java.util.List;

public class WorkoutObject {

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
        return (data == null) ? data = new DataBean() : data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String id;
        private String user_id;
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

        public double getTotal_distance() {
            return total_distance;
        }

        public void setTotal_distance(double total_distance) {
            this.total_distance = total_distance;
        }

        public List<ActivityInfoBean> getActivity_info() {
            return (activity_info == null) ? activity_info = new ArrayList<>() : activity_info;
        }

        public void setActivity_info(List<ActivityInfoBean> activity_info) {
            this.activity_info = activity_info;
        }

    }
}
