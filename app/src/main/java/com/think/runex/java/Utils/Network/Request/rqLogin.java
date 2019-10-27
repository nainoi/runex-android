package com.think.runex.java.Utils.Network.Request;

public class rqLogin {

    /**
     * email : fakespmh.21@gmail.com
     * password : p@ss1234
     * PF : web
     */

    public rqLogin(){}
    public rqLogin(String email, String password, String platform){
        this.email = email;
        this.password = password;
        this.PF = platform;
    }
    private String email;
    private String password;
    private String PF;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPF() {
        return PF;
    }

    public void setPF(String PF) {
        this.PF = PF;
    }
}
