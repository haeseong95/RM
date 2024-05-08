package com.example.rm;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

// retrofit api 데이터 클래스
public class RetroApi {
    @SerializedName("Users")
    private ArrayList<RetroUser> retroUser;   // User 클래스의 필드 형태를 객체로 사용
    public ArrayList<RetroUser> getUser() {
        return retroUser;
    }
    public void setUser(ArrayList<RetroUser> retroWriting) {
        this.retroUser = retroWriting;
    }
}