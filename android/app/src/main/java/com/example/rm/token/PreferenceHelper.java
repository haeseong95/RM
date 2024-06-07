package com.example.rm.token;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

// 로그인 상태 유지
public class PreferenceHelper {
    private static final String tag = "preference 헬퍼";
    private static final String SHARED_PREF_NAME = "sharePref";
    private static final String LOGIN_IN = "loginIn";   // 로그인 상태를 저장하는 키
    private static final String USER_ID = "userId";     // 로그인 시 입력한 아이디를 저장하는 키
    private static SharedPreferences sharedPreferences;     // key-value로 데이터 저장

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    // 로그인 상태 저장
    public static void setLoginState(Context context, boolean login, String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_IN, login);
        editor.putString(USER_ID, id);
        editor.apply();
        Log.d(tag + " 로그인 상태", "로그인O(true) / 로그인X(false) : " + sharedPreferences.getBoolean(LOGIN_IN, false) + ", 로그인 아이디 : " +id);
    }

    // 메인화면의 로그인/마이 텍스트 변경
    public static boolean getLoginState(){
        Log.i(tag, "마이페이지(true) / 로그인(false) : " + sharedPreferences.getBoolean(LOGIN_IN, false));
        return sharedPreferences.getBoolean(LOGIN_IN, false);
    }

    // 로그인 시 사용된 아이디 문자열 형태로 반환
    public static String getLoginId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(USER_ID, "");    // id키가 없으면 문자열"" 반환
        Log.i("Preference, getLoginId", "로그인 시 사용된 아이디 값 : " + id);
        return id;
    }

    // 로그아웃 처리 : 모든 데이터 초기화
    public static void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.d(tag, "사용자 로그아웃 성공");
    }
}