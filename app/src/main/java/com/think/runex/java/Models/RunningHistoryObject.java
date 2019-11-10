package com.think.runex.java.Models;

import java.util.List;

public class RunningHistoryObject {

    /**
     * code : 200
     * msg : ok
     * data : [{"id":"5dc550ccfc15a8f6bf32c846","user_id":"5d95aee5e75267679685f9df","activity_info":[{"id":"5dc550ccfc15a8f6bf32c845","activity_type":"RUN","calory":0,"caption":"Hello Postman","distance":1.5,"pace":3.98,"time":24.3489,"activity_date":"2019-11-08T11:26:04.566Z","created_at":"2019-11-08T11:26:04.566Z","updated_at":"2019-11-08T11:26:04.566Z"},{"id":"5dc550d4fc15a8f6bf32c847","activity_type":"RUN","calory":3.178593,"caption":"","distance":99.93218,"pace":3.9801297,"time":25.428743,"activity_date":"2019-11-08T11:26:12.787Z","created_at":"2019-11-08T11:26:12.787Z","updated_at":"2019-11-08T11:26:12.787Z"},{"id":"5dc550eafc15a8f6bf32c848","activity_type":"RUN","calory":3.178593,"caption":"","distance":99.93218,"pace":3.9801297,"time":25.428743,"activity_date":"2019-11-08T11:26:34.027Z","created_at":"2019-11-08T11:26:34.027Z","updated_at":"2019-11-08T11:26:34.027Z"},{"id":"5dc5b73bfc15a8f6bf32c88c","activity_type":"RUN","calory":0,"caption":"","distance":0.0023169795,"pace":35.95,"time":0.083333336,"activity_date":"2019-11-08T18:43:07.142Z","created_at":"2019-11-08T18:43:07.142Z","updated_at":"2019-11-08T18:43:07.142Z"},{"id":"5dc5b998fc15a8f6bf32c88d","activity_type":"RUN","calory":0,"caption":"","distance":0.09221184,"pace":19.7,"time":1.8166667,"activity_date":"2019-11-08T18:53:12.073Z","created_at":"2019-11-08T18:53:12.073Z","updated_at":"2019-11-08T18:53:12.073Z"},{"id":"5dc5bd7cfc15a8f6bf32c88e","activity_type":"RUN","calory":0,"caption":"","distance":0.10357125,"pace":23.8,"time":2.4666667,"activity_date":"2019-11-08T19:09:48.695Z","created_at":"2019-11-08T19:09:48.695Z","updated_at":"2019-11-08T19:09:48.695Z"},{"id":"5dc68af4fc15a8f6bf32c8af","activity_type":"RUN","calory":0,"caption":"","distance":0.19397157,"pace":1188.65,"time":230.6,"activity_date":"2019-11-09T09:46:28.466Z","created_at":"2019-11-09T09:46:28.466Z","updated_at":"2019-11-09T09:46:28.466Z"},{"id":"5dc6e701fc15a8f6bf32c8e4","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:19:13.574Z","created_at":"2019-11-09T16:19:13.574Z","updated_at":"2019-11-09T16:19:13.574Z"},{"id":"5dc6e725fc15a8f6bf32c8e5","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:19:49.68Z","created_at":"2019-11-09T16:19:49.68Z","updated_at":"2019-11-09T16:19:49.68Z"},{"id":"5dc6e735fc15a8f6bf32c8e6","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:20:05.501Z","created_at":"2019-11-09T16:20:05.501Z","updated_at":"2019-11-09T16:20:05.501Z"},{"id":"5dc6e835fc15a8f6bf32c8e7","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:24:21.416Z","created_at":"2019-11-09T16:24:21.416Z","updated_at":"2019-11-09T16:24:21.416Z"},{"id":"5dc79832fc15a8f6bf32c907","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:14.243Z","created_at":"2019-11-10T04:55:14.243Z","updated_at":"2019-11-10T04:55:14.243Z"},{"id":"5dc79835fc15a8f6bf32c908","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:17.368Z","created_at":"2019-11-10T04:55:17.368Z","updated_at":"2019-11-10T04:55:17.368Z"},{"id":"5dc79838fc15a8f6bf32c909","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:20.754Z","created_at":"2019-11-10T04:55:20.754Z","updated_at":"2019-11-10T04:55:20.754Z"},{"id":"5dc79853fc15a8f6bf32c90a","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:47.164Z","created_at":"2019-11-10T04:55:47.164Z","updated_at":"2019-11-10T04:55:47.164Z"},{"id":"5dc7985bfc15a8f6bf32c90b","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:55.687Z","created_at":"2019-11-10T04:55:55.687Z","updated_at":"2019-11-10T04:55:55.687Z"},{"id":"5dc79b8cfc15a8f6bf32c90f","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:09:32.86Z","created_at":"2019-11-10T05:09:32.86Z","updated_at":"2019-11-10T05:09:32.86Z"},{"id":"5dc79b90fc15a8f6bf32c910","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:09:36.75Z","created_at":"2019-11-10T05:09:36.75Z","updated_at":"2019-11-10T05:09:36.75Z"},{"id":"5dc79f50fc15a8f6bf32c912","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:36.904Z","created_at":"2019-11-10T05:25:36.904Z","updated_at":"2019-11-10T05:25:36.904Z"},{"id":"5dc79f51fc15a8f6bf32c913","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:37.568Z","created_at":"2019-11-10T05:25:37.568Z","updated_at":"2019-11-10T05:25:37.568Z"},{"id":"5dc79f60fc15a8f6bf32c914","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:52.714Z","created_at":"2019-11-10T05:25:52.714Z","updated_at":"2019-11-10T05:25:52.714Z"},{"id":"5dc7b676fc15a8f6bf32c91b","activity_type":"RUN","calory":0,"caption":"","distance":2.3253185E-4,"pace":573.38336,"time":0.18333334,"activity_date":"2019-11-10T07:04:22.331Z","created_at":"2019-11-10T07:04:22.331Z","updated_at":"2019-11-10T07:04:22.331Z"},{"id":"5dc7b6f6fc15a8f6bf32c91c","activity_type":"RUN","calory":0,"caption":"","distance":0.00125759,"pace":53,"time":0.083333336,"activity_date":"2019-11-10T07:06:30.698Z","created_at":"2019-11-10T07:06:30.698Z","updated_at":"2019-11-10T07:06:30.698Z"},{"id":"5dc7b750fc15a8f6bf32c91d","activity_type":"RUN","calory":0,"caption":"","distance":0.0032987448,"pace":277.88333,"time":0.96666664,"activity_date":"2019-11-10T07:08:00.545Z","created_at":"2019-11-10T07:08:00.545Z","updated_at":"2019-11-10T07:08:00.545Z"},{"id":"5dc7b762fc15a8f6bf32c91e","activity_type":"RUN","calory":0,"caption":"","distance":0.0032987448,"pace":277.88333,"time":0.96666664,"activity_date":"2019-11-10T07:08:18.119Z","created_at":"2019-11-10T07:08:18.119Z","updated_at":"2019-11-10T07:08:18.119Z"},{"id":"5dc7b7b4fc15a8f6bf32c91f","activity_type":"RUN","calory":0,"caption":"","distance":1.0362043,"pace":0,"time":0.033333335,"activity_date":"2019-11-10T07:09:40.693Z","created_at":"2019-11-10T07:09:40.693Z","updated_at":"2019-11-10T07:09:40.693Z"},{"id":"5dc7b826fc15a8f6bf32c922","activity_type":"RUN","calory":0,"caption":"","distance":1.0390253,"pace":0.016666668,"time":0.033333335,"activity_date":"2019-11-10T07:11:34.118Z","created_at":"2019-11-10T07:11:34.118Z","updated_at":"2019-11-10T07:11:34.118Z"},{"id":"5dc7b8d2fc15a8f6bf32c923","activity_type":"RUN","calory":0,"caption":"","distance":1.0364891,"pace":0,"time":0.016666668,"activity_date":"2019-11-10T07:14:26.419Z","created_at":"2019-11-10T07:14:26.419Z","updated_at":"2019-11-10T07:14:26.419Z"},{"id":"5dc7b930fc15a8f6bf32c924","activity_type":"RUN","calory":0,"caption":"","distance":1.036512,"pace":0.016666668,"time":0.06666667,"activity_date":"2019-11-10T07:16:00.48Z","created_at":"2019-11-10T07:16:00.48Z","updated_at":"2019-11-10T07:16:00.48Z"}],"total_distance":230.13008}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5dc550ccfc15a8f6bf32c846
         * user_id : 5d95aee5e75267679685f9df
         * activity_info : [{"id":"5dc550ccfc15a8f6bf32c845","activity_type":"RUN","calory":0,"caption":"Hello Postman","distance":1.5,"pace":3.98,"time":24.3489,"activity_date":"2019-11-08T11:26:04.566Z","created_at":"2019-11-08T11:26:04.566Z","updated_at":"2019-11-08T11:26:04.566Z"},{"id":"5dc550d4fc15a8f6bf32c847","activity_type":"RUN","calory":3.178593,"caption":"","distance":99.93218,"pace":3.9801297,"time":25.428743,"activity_date":"2019-11-08T11:26:12.787Z","created_at":"2019-11-08T11:26:12.787Z","updated_at":"2019-11-08T11:26:12.787Z"},{"id":"5dc550eafc15a8f6bf32c848","activity_type":"RUN","calory":3.178593,"caption":"","distance":99.93218,"pace":3.9801297,"time":25.428743,"activity_date":"2019-11-08T11:26:34.027Z","created_at":"2019-11-08T11:26:34.027Z","updated_at":"2019-11-08T11:26:34.027Z"},{"id":"5dc5b73bfc15a8f6bf32c88c","activity_type":"RUN","calory":0,"caption":"","distance":0.0023169795,"pace":35.95,"time":0.083333336,"activity_date":"2019-11-08T18:43:07.142Z","created_at":"2019-11-08T18:43:07.142Z","updated_at":"2019-11-08T18:43:07.142Z"},{"id":"5dc5b998fc15a8f6bf32c88d","activity_type":"RUN","calory":0,"caption":"","distance":0.09221184,"pace":19.7,"time":1.8166667,"activity_date":"2019-11-08T18:53:12.073Z","created_at":"2019-11-08T18:53:12.073Z","updated_at":"2019-11-08T18:53:12.073Z"},{"id":"5dc5bd7cfc15a8f6bf32c88e","activity_type":"RUN","calory":0,"caption":"","distance":0.10357125,"pace":23.8,"time":2.4666667,"activity_date":"2019-11-08T19:09:48.695Z","created_at":"2019-11-08T19:09:48.695Z","updated_at":"2019-11-08T19:09:48.695Z"},{"id":"5dc68af4fc15a8f6bf32c8af","activity_type":"RUN","calory":0,"caption":"","distance":0.19397157,"pace":1188.65,"time":230.6,"activity_date":"2019-11-09T09:46:28.466Z","created_at":"2019-11-09T09:46:28.466Z","updated_at":"2019-11-09T09:46:28.466Z"},{"id":"5dc6e701fc15a8f6bf32c8e4","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:19:13.574Z","created_at":"2019-11-09T16:19:13.574Z","updated_at":"2019-11-09T16:19:13.574Z"},{"id":"5dc6e725fc15a8f6bf32c8e5","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:19:49.68Z","created_at":"2019-11-09T16:19:49.68Z","updated_at":"2019-11-09T16:19:49.68Z"},{"id":"5dc6e735fc15a8f6bf32c8e6","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:20:05.501Z","created_at":"2019-11-09T16:20:05.501Z","updated_at":"2019-11-09T16:20:05.501Z"},{"id":"5dc6e835fc15a8f6bf32c8e7","activity_type":"RUN","calory":0,"caption":"","distance":5.9502172,"pace":5.616667,"time":33.483334,"activity_date":"2019-11-09T16:24:21.416Z","created_at":"2019-11-09T16:24:21.416Z","updated_at":"2019-11-09T16:24:21.416Z"},{"id":"5dc79832fc15a8f6bf32c907","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:14.243Z","created_at":"2019-11-10T04:55:14.243Z","updated_at":"2019-11-10T04:55:14.243Z"},{"id":"5dc79835fc15a8f6bf32c908","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:17.368Z","created_at":"2019-11-10T04:55:17.368Z","updated_at":"2019-11-10T04:55:17.368Z"},{"id":"5dc79838fc15a8f6bf32c909","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:20.754Z","created_at":"2019-11-10T04:55:20.754Z","updated_at":"2019-11-10T04:55:20.754Z"},{"id":"5dc79853fc15a8f6bf32c90a","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:47.164Z","created_at":"2019-11-10T04:55:47.164Z","updated_at":"2019-11-10T04:55:47.164Z"},{"id":"5dc7985bfc15a8f6bf32c90b","activity_type":"RUN","calory":0,"caption":"","distance":0.029440193,"pace":3.95,"time":0.13333334,"activity_date":"2019-11-10T04:55:55.687Z","created_at":"2019-11-10T04:55:55.687Z","updated_at":"2019-11-10T04:55:55.687Z"},{"id":"5dc79b8cfc15a8f6bf32c90f","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:09:32.86Z","created_at":"2019-11-10T05:09:32.86Z","updated_at":"2019-11-10T05:09:32.86Z"},{"id":"5dc79b90fc15a8f6bf32c910","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:09:36.75Z","created_at":"2019-11-10T05:09:36.75Z","updated_at":"2019-11-10T05:09:36.75Z"},{"id":"5dc79f50fc15a8f6bf32c912","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:36.904Z","created_at":"2019-11-10T05:25:36.904Z","updated_at":"2019-11-10T05:25:36.904Z"},{"id":"5dc79f51fc15a8f6bf32c913","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:37.568Z","created_at":"2019-11-10T05:25:37.568Z","updated_at":"2019-11-10T05:25:37.568Z"},{"id":"5dc79f60fc15a8f6bf32c914","activity_type":"RUN","calory":0,"caption":"","distance":0.053858213,"pace":5.866667,"time":0.33333334,"activity_date":"2019-11-10T05:25:52.714Z","created_at":"2019-11-10T05:25:52.714Z","updated_at":"2019-11-10T05:25:52.714Z"},{"id":"5dc7b676fc15a8f6bf32c91b","activity_type":"RUN","calory":0,"caption":"","distance":2.3253185E-4,"pace":573.38336,"time":0.18333334,"activity_date":"2019-11-10T07:04:22.331Z","created_at":"2019-11-10T07:04:22.331Z","updated_at":"2019-11-10T07:04:22.331Z"},{"id":"5dc7b6f6fc15a8f6bf32c91c","activity_type":"RUN","calory":0,"caption":"","distance":0.00125759,"pace":53,"time":0.083333336,"activity_date":"2019-11-10T07:06:30.698Z","created_at":"2019-11-10T07:06:30.698Z","updated_at":"2019-11-10T07:06:30.698Z"},{"id":"5dc7b750fc15a8f6bf32c91d","activity_type":"RUN","calory":0,"caption":"","distance":0.0032987448,"pace":277.88333,"time":0.96666664,"activity_date":"2019-11-10T07:08:00.545Z","created_at":"2019-11-10T07:08:00.545Z","updated_at":"2019-11-10T07:08:00.545Z"},{"id":"5dc7b762fc15a8f6bf32c91e","activity_type":"RUN","calory":0,"caption":"","distance":0.0032987448,"pace":277.88333,"time":0.96666664,"activity_date":"2019-11-10T07:08:18.119Z","created_at":"2019-11-10T07:08:18.119Z","updated_at":"2019-11-10T07:08:18.119Z"},{"id":"5dc7b7b4fc15a8f6bf32c91f","activity_type":"RUN","calory":0,"caption":"","distance":1.0362043,"pace":0,"time":0.033333335,"activity_date":"2019-11-10T07:09:40.693Z","created_at":"2019-11-10T07:09:40.693Z","updated_at":"2019-11-10T07:09:40.693Z"},{"id":"5dc7b826fc15a8f6bf32c922","activity_type":"RUN","calory":0,"caption":"","distance":1.0390253,"pace":0.016666668,"time":0.033333335,"activity_date":"2019-11-10T07:11:34.118Z","created_at":"2019-11-10T07:11:34.118Z","updated_at":"2019-11-10T07:11:34.118Z"},{"id":"5dc7b8d2fc15a8f6bf32c923","activity_type":"RUN","calory":0,"caption":"","distance":1.0364891,"pace":0,"time":0.016666668,"activity_date":"2019-11-10T07:14:26.419Z","created_at":"2019-11-10T07:14:26.419Z","updated_at":"2019-11-10T07:14:26.419Z"},{"id":"5dc7b930fc15a8f6bf32c924","activity_type":"RUN","calory":0,"caption":"","distance":1.036512,"pace":0.016666668,"time":0.06666667,"activity_date":"2019-11-10T07:16:00.48Z","created_at":"2019-11-10T07:16:00.48Z","updated_at":"2019-11-10T07:16:00.48Z"}]
         * total_distance : 230.13008
         */

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
            return activity_info;
        }

        public void setActivity_info(List<ActivityInfoBean> activity_info) {
            this.activity_info = activity_info;
        }

        public static class ActivityInfoBean {
            /**
             * id : 5dc550ccfc15a8f6bf32c845
             * activity_type : RUN
             * calory : 0
             * caption : Hello Postman
             * distance : 1.5
             * pace : 3.98
             * time : 24.3489
             * activity_date : 2019-11-08T11:26:04.566Z
             * created_at : 2019-11-08T11:26:04.566Z
             * updated_at : 2019-11-08T11:26:04.566Z
             */

            private String id;
            private String activity_type;
            private double calory;
            private String caption;
            private double distance;
            private double pace;
            private double time;
            private String activity_date;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getActivity_type() {
                return activity_type;
            }

            public void setActivity_type(String activity_type) {
                this.activity_type = activity_type;
            }

            public double getCalory() {
                return calory;
            }

            public void setCalory(double calory) {
                this.calory = calory;
            }

            public String getCaption() {
                return caption;
            }

            public void setCaption(String caption) {
                this.caption = caption;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getPace() {
                return pace;
            }

            public void setPace(double pace) {
                this.pace = pace;
            }

            public double getTime() {
                return time;
            }

            public void setTime(double time) {
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
        }
    }
}
