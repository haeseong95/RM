package com.example.rm;

import com.google.gson.annotations.SerializedName;

// Retrofit의 게시글 Data class
public class RetroWriting {

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RetroWriting(String hash, String author, String title, String type, String content, String create, String modify, String delete, String like, String category, String image) {
        this.hash = hash;
        this.author = author;
        this.title = title;
        this.type = type;
        this.content = content;
        this.create = create;
        this.modify = modify;
        this.delete = delete;
        this.like = like;
        this.category = category;
        this.image = image;
    }
}