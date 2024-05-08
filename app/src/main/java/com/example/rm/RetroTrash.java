package com.example.rm;

import com.google.gson.annotations.SerializedName;

// Retrofit의 쓰레기 Data class (JSON -> JAVA 변환)
public class RetroTrash {

    @SerializedName("")       // "JSON 키"
    private String id;      // java에서 사용할 변수 이름

    @SerializedName("")
    private String trashName;   // 쓰레기 이름

    private int title;

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

    public RetroTrash(String id){
        this.id = id;
    }

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