package com.example.rm;


import com.google.gson.annotations.SerializedName;

// retrofit user 데이터 클래스
public class RetroUser {

    // jsonplaceholder 테스트
    private int albumId;
    private int id;
    private String title;
    private String body;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public RetroUser(String title, String body, String image) {
        this.title = title;
        this.body = body;
        this.url = image;
    }

    /*
    // 원래 코드
    private String email;
    private String id;
    private String nickname;
    private String place;   // 사용자 랭킹
    private String status;  // 사용자 상태
    @SerializedName("passwd")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", place='" + place + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

 */
}
