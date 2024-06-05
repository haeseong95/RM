package com.example.rm.retrofit;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

// retrofit api 데이터 클래스
public class RetroApi {

    // User 클래스의 필드 형태를 객체로 사용
    @SerializedName("Users")
    private ArrayList<RetroUser> retroUser;
    public ArrayList<RetroUser> getRetroUser() {return retroUser;}
    public void setRetroUser(ArrayList<RetroUser> retroUser) {this.retroUser = retroUser;}

    // Writing 클래스
    @SerializedName("Writing")
    private ArrayList<RetroWriting> retroWriting;
    public ArrayList<RetroWriting> getRetroWriting() {return retroWriting;}
    public void setRetroWriting(ArrayList<RetroWriting> retroWriting) {this.retroWriting = retroWriting;}
}