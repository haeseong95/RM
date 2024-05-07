package com.example.rm;

import com.google.gson.annotations.SerializedName;

// Retrofit의 쓰레기 Data class (JSON -> JAVA 변환)
public class RetroTrash {

    @SerializedName("")       // "JSON 키"
    private String id;      // java에서 사용할 변수 이름

    @SerializedName("")
    private String trashName;   // 쓰레기 이름

    private int title;      // 키=변수명이 같으면 직렬화 안해도 됨

    @SerializedName("trash_image")
    private String trashImage;

    @SerializedName("trash_tag")
    private String trashTag;

    @SerializedName("trash_info")
    private String trashInfo;

    // getter, setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // 생성자 : 멤버 변수 초기화
    public RetroTrash(String id){
        this.id = id;
    }

    // toString()을 오버라이드 하지 않으면 객체 주소값을 출력함
    // getter()가 개별 멤버변수 값에 접근 <-> toString()은 여러 개의 멤버변수 값에 접근

    @Override
    public String toString() {
        return "RetroTrash{" +
                "id='" + id + '\'' +
                ", trashName='" + trashName + '\'' +
                ", title=" + title +
                ", trashImage='" + trashImage + '\'' +
                ", trashTag='" + trashTag + '\'' +
                ", trashInfo='" + trashInfo + '\'' +
                '}';
    }


}