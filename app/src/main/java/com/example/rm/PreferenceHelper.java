package com.example.rm;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

// 로그인 상태 유지
public class PreferenceHelper {

    private static final String SHARED_PREF_NAME = "sharePref";
    private static final String LOGIN_IN = "loginIn";   // 로그인 상태를 저장할 키
    private static SharedPreferences sharedPreferences;     // key-value로 데이터 저장

    public static void init(Context context) {      // 앱 시작 시 PreferenceHelper.init(this)를 호출, 모든 액티비티에서 인스턴스 얻음
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);   // 자기 앱 내에서만 사용 (외부 앱 접근X)
    }

    // 로그인O true / 로그인X false : 로그인 성공했으므로 true 값 저장
    public static void setLoginState(Context context, boolean login, String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_IN, login);
        editor.putString("id", id);     // 로그인 시 입력한 아이디 값 저장
        editor.apply();

        Log.d("Preference", "LOGIN_IN 상태 (" + sharedPreferences.getBoolean(LOGIN_IN, false) + ") : 로그인O(true) / 로그인X(false)" + "       로그인 아이디 저장 : " + id);
    }

    // 메인페이지에서 로그인/마이페이지 텍스트 변경을 위한 메소드
    public static boolean getLoginState(){
        Log.i("Preference : LOGIN_IN", "마이페이지(true) / 로그인(false) : " + sharedPreferences.getBoolean(LOGIN_IN, false));
        return sharedPreferences.getBoolean(LOGIN_IN, false);   // LOGIN_ID 키로 저장된 로그인 상태 조회
    }

    // 저장된 로그인 정보 가져오기
    public static String getLoginId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString("id", "");    // id키가 없으면 문자열"" 반환
        Log.i("Preference, getLoginId", "저장된 아이디 정보 가져오기 : " + id);
        return id;
    }

    // 로그아웃 처리 (모든 데이터 초기화)
    public static void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();     // 데이터 삭제
        editor.apply();
        Log.d("Preference : 로그아웃", "사용자 로그아웃 성공");
    }

}
