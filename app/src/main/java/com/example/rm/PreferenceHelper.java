package com.example.rm;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

// 로그인 상태 유지
public class PreferenceHelper {
    private static final String tag = "preference";
    private static final String SHARED_PREF_NAME = "sharePref";
    private static final String LOGIN_IN = "loginIn";   // 로그인 상태를 저장하는 키
    private static final String USER_ID = "userId";     // 로그인 시 입력한 아이디를 저장하는 키
    private static SharedPreferences sharedPreferences;     // key-value로 데이터 저장

    public static void init(Context context) {      // 앱 시작 시 PreferenceHelper.init(this)를 호출, 모든 액티비티에서 인스턴스 얻음
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);   // 자기 앱 내에서만 사용 (외부 앱 접근X)
    }

    // 로그인O true / 로그인X false : 로그인 성공했으므로 true 값 저장
    public static void setLoginState(Context context, boolean login, String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_IN, login);
        editor.putString(USER_ID, id);
        editor.apply();
        Log.d(tag + " 로그인 상태", "로그인O(true) / 로그인X(false) : " + sharedPreferences.getBoolean(LOGIN_IN, false) + "로그인 아이디" +id);
    }

    // 메인페이지에서 로그인/마이페이지 텍스트 변경을 위한 메소드
    public static boolean getLoginState(){
        Log.i("Preference : LOGIN_IN", "마이페이지(true) / 로그인(false) : " + sharedPreferences.getBoolean(LOGIN_IN, false));
        return sharedPreferences.getBoolean(LOGIN_IN, false);   // LOGIN_ID 키로 저장된 로그인 상태 조회
    }

    // 로그인 시 사용된 아이디 문자열 형태로 반환
    public static String getLoginId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(USER_ID, "");    // id키가 없으면 문자열"" 반환
        Log.i("Preference, getLoginId", "로그인 시 사용된 아이디 값 : " + id);
        return id;
    }

    // 로그아웃 처리 (모든 데이터 초기화)
    public static void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();     // 데이터 삭제
        editor.apply();
        Log.d("Preference : 로그아웃", "사용자 로그아웃 성공");
    }

    // 좋아요 상태 저장
    public static void likeState(String postId, boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("likeState_" + postId, state);   // 키 값이 postId로 해서 저장하면 각 postid마다 상태값이 저장될듯, 근데 boolean과 int가 타입이 다른데 같은 키값을 사용하면 안되므로 앞에 각각 다른 키 사용 필요
        editor.apply();
        Log.d(tag + ", 좋아요 상태", "좋아요 눌린 상태(true) / 안누름(false) : " + sharedPreferences.getBoolean("likeState_" + postId, false));
    }

    // 현재 좋아요 상태 조회
    public static boolean getLikeState(String postId){
        Log.i(tag + ", 초기 좋아요 상태", "좋아요 눌린 상태(true) / 안누름(false) : " + sharedPreferences.getBoolean("likeState_" + postId, false));
        return sharedPreferences.getBoolean("likeState_" + postId, false);
    }

    // 좋아요 개수 저장
    public static void likeCount(String postId, int count){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("likeCount_" + postId, count);   // 키 값이 postId로 해서 저장하면 각 postid마다 상태값이 저장될듯
        editor.apply();
        Log.i(tag + ", 좋아요 개수", "좋아요 개수 : " + sharedPreferences.getInt("likeCount_" + postId, count));
    }

    // 현재 좋아요 개수 조회
    public static int getLikeCount(String postId){
        Log.i(tag + ", 초기 좋아요 개수", "좋아요 개수 : " + sharedPreferences.getInt("likeCount_" + postId, 0));
        return sharedPreferences.getInt("likeCount_" + postId, 0);
    }

    // 로그인 id를 이용해 내가 작성한 게시글이 맞는지 판별 (true - 내 거 맞음, false-남의 거)
    public static boolean checkMyPost(Context context, String id){
        String userId = getLoginId(context);    // 로그인 시 사용된 아이디값 저장됨
        return userId.equals(id);
    }


}
