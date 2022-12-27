package com.lee.practice.mybatisdemo.dto;

public class User {

    private String id;
    private String pw;
    private String sessionid;
    private String logindate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public boolean matchPassword(String newPassword) {
        if (newPassword == null) {
            return false;
        }
        return newPassword.equals(pw);
    }

    public boolean matchId(String newId) {
        if (newId == null) {
            return false;
        }
        return newId.equals(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", logindate='" + logindate + '\'' +
                '}';
    }
}
