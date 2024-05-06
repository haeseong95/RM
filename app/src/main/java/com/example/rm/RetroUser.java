package com.example.rm;

import com.google.gson.annotations.SerializedName;

// Retrofit의 사용자 Data class
public class RetroUser {

    @SerializedName("userId")   // "json키"
    private int userId;

    private int id;

    @SerializedName("title")
    private String uTitle;

    @SerializedName("body")
    private String uBody;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuTitle() {
        return uTitle;
    }

    public void setuTitle(String uTitle) {
        this.uTitle = uTitle;
    }

    public String getuBody() {
        return uBody;
    }

    public void setuBody(String uBody) {
        this.uBody = uBody;
    }

    // toString을 오버라이드 해주지 않으면 객체 주소값을 출력함

    @Override
    public String toString() {
        return "RetroUser{" +
                "userId=" + userId +
                ", id=" + id +
                ", uTitle='" + uTitle + '\'' +
                ", uBody='" + uBody + '\'' +
                '}';
    }
}