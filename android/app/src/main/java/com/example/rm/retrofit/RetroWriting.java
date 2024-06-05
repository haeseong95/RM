package com.example.rm.retrofit;

import com.google.gson.annotations.SerializedName;

// Retrofit의 게시글 Writing 데이터 클래스
public class RetroWriting {

    /*
    private String hash;    // 글 구별을 위한 고유값
    private String author;  // 글 작성자
    private String title;   // 글 제목
    private String type;    // 게시글, 댓글, 공지사항, 카테고리, 쓰레기 중 하나의 게시글 종류

    @SerializedName("contentFileLocation")
    private String content;     // 파일 위치 (카테고리의 메인 설명)

    @SerializedName("createTime")
    private String create;  // 글이 생성된 시간

    @SerializedName("modifyTime")
    private String modify;  // 글을 수정된 시간

    @SerializedName("deleteTime")
    private String delete;  // 글이 삭제된 시간

    @SerializedName("thumbsUp")
    private String like;    // 추천 수

    @SerializedName("whichWriting")
    private String category;    // 카테고리 10종류 구분

    @SerializedName("fileType")
    private String image;   // 이미지가 저장된 경로, PNG/JPG 형태로 저장된 String 타입임



     */




    // jsonplaceholder/photos 테스트
    private int albumId;
    private int id;
    private String title;
    private String thumbnailUrl;
    private String url;

    public RetroWriting(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}