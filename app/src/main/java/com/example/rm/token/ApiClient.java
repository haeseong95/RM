package com.example.rm.token;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rm.LoginUser;
import com.example.rm.token.TokenManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiClient {

    public static OkHttpClient getClient(final Context context, final TokenManager tokenManager) {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request original = chain.request();

                        // 토큰을 헤더에 추가
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", tokenManager.getToken());

                        Request request = requestBuilder.build();
                        Response response = chain.proceed(request);

                        if (response.code() == 401) {  // 토큰 만료
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tokenManager.clearToken();
                                    Toast.makeText(context, "사용시간이 초과되었습니다. 다시 로그인 부탁드립니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, LoginUser.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }
                            });
                        }

                        return response;
                    }
                })
                .build();
    }

    private static void runOnUiThread(Runnable runnable) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(runnable);
    }
}
