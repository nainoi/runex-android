package com.think.runex.java.Models;

import java.util.List;

public class UserObject {

    /**
     * data : {"email":"suthisak.ch@gmail.com","fullname":"suthisak chuejit","firstname":"suthisak","firstname_th":"สุทธิศักดิ์","lastname_th":"ชื่นจิตร","lastname":"chuenjit","phone":"","avatar":"","role":"ADMIN","birthdate":"0001-01-01T00:00:00Z","gender":"","created_at":"2019-09-14T08:33:33.103Z","updated_at":"2019-09-30T13:06:03.847Z","confirm":false,"address":[{"id":"5d91fdbba44645f9678fa3cc","address":"987 หมู่ 8","province":"ขอนแก่น","district":"เมืองขอนแก่น","city":"บ้านเป็ด","zipcode":"40000","created_at":"2019-09-30T13:06:03.847Z","updated_at":"2019-09-30T13:06:03.847Z"}]}
     * msg : success
     */

    private DataBean data = new DataBean();
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * email : suthisak.ch@gmail.com
         * fullname : suthisak chuejit
         * firstname : suthisak
         * firstname_th : สุทธิศักดิ์
         * lastname_th : ชื่นจิตร
         * lastname : chuenjit
         * phone :
         * avatar :
         * role : ADMIN
         * birthdate : 0001-01-01T00:00:00Z
         * gender :
         * created_at : 2019-09-14T08:33:33.103Z
         * updated_at : 2019-09-30T13:06:03.847Z
         * confirm : false
         * address : [{"id":"5d91fdbba44645f9678fa3cc","address":"987 หมู่ 8","province":"ขอนแก่น","district":"เมืองขอนแก่น","city":"บ้านเป็ด","zipcode":"40000","created_at":"2019-09-30T13:06:03.847Z","updated_at":"2019-09-30T13:06:03.847Z"}]
         */

        private String email;
        private String fullname;
        private String firstname;
        private String firstname_th;
        private String lastname_th;
        private String lastname;
        private String phone;
        private String avatar;
        private String role;
        private String birthdate;
        private String gender;
        private String created_at;
        private String updated_at;
        private boolean confirm;
        private List<AddressBean> address;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getFirstname_th() {
            return firstname_th;
        }

        public void setFirstname_th(String firstname_th) {
            this.firstname_th = firstname_th;
        }

        public String getLastname_th() {
            return lastname_th;
        }

        public void setLastname_th(String lastname_th) {
            this.lastname_th = lastname_th;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public boolean isConfirm() {
            return confirm;
        }

        public void setConfirm(boolean confirm) {
            this.confirm = confirm;
        }

        public List<AddressBean> getAddress() {
            return address;
        }

        public void setAddress(List<AddressBean> address) {
            this.address = address;
        }

        public static class AddressBean {
            /**
             * id : 5d91fdbba44645f9678fa3cc
             * address : 987 หมู่ 8
             * province : ขอนแก่น
             * district : เมืองขอนแก่น
             * city : บ้านเป็ด
             * zipcode : 40000
             * created_at : 2019-09-30T13:06:03.847Z
             * updated_at : 2019-09-30T13:06:03.847Z
             */

            private String id;
            private String address;
            private String province;
            private String district;
            private String city;
            private String zipcode;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
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
