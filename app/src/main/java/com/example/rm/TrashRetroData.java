package com.example.rm;

import com.google.gson.annotations.SerializedName;

// Retrofit의 데이터 클래스
public class TrashRetroData {

    @SerializedName("id")
    private String id;


    // 쓰레기 정보 : Mysql 속성명, 타입에 맞게 생성하기
    @SerializedName("trash_name")
    private String trashName;

    @SerializedName("trash_image")
    private String trashImage;

    @SerializedName("trash_tag")
    private String trashTag;

    @SerializedName("trash_info")
    private String trashInfo;

    @Override
    public String toString(){
        return "RetroData{userId = " + id + '}';
    }


}
