package com.example.rm.token;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rm.PreferenceHelper;

public class TokenManager {
    private static final String SHARED_PREFS_NAME = "token_prefs";
    private static final String TOKEN_KEY = "auth_token";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        PreferenceHelper.logout();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }

    // 토큰 값을 이용해 로그인 상태 확인 (true-로그인O / false-로그인X)
    public boolean LoginState(Context context){
        String token = getToken();
        return token != null && token.isEmpty();
    }
}
