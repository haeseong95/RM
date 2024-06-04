package com.example.rm.mypage;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.token.ApiClient;
import com.example.rm.token.JWTUtils;
import com.example.rm.token.TokenManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MypageUserInfo extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    Button btnPwd, btnNickname;
    static TextView textViewNickname;
    TextView textViewEmail;
    TextView textViewId;
    private static final String tag = "회원정보 수정, MypageUserInfo";
    TokenManager tokenManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modify);
        imageView = findViewById(R.id.btn_back);
        textViewId = findViewById(R.id.text_modify_id);
        btnPwd = findViewById(R.id.btn_modify_password);
        btnNickname = findViewById(R.id.btn_modify_nickname);
        textViewNickname = findViewById(R.id.text_modify_nickname);
        textViewEmail = findViewById(R.id.text_modify_email);

        imageView.setOnClickListener(this);
        btnPwd.setOnClickListener(this);
        btnNickname.setOnClickListener(this);
        tokenManager = new TokenManager(this);

        getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: // 뒤로 가기
                finish();
                break;
            case R.id.btn_modify_password:  // 비밀번호 수정
                PasswordBottomSheet passwordBottomSheet = new PasswordBottomSheet();
                passwordBottomSheet.show(getSupportFragmentManager(), "비밀번호 변경");
                break;
            case R.id.btn_modify_nickname:  // 닉네임 수정
                NickNameBottomSheet nickNameBottomSheet = new NickNameBottomSheet();
                nickNameBottomSheet.show(getSupportFragmentManager(), "닉네임 변경");
                break;
            default:
                throw new IllegalStateException("버튼 이동 오류남: " + v.getId());
        }
    }

    // 사용자 정보 가져옴
    private void getUserInfo() {
        new Thread(() -> {
            OkHttpClient client = ApiClient.getClient(MypageUserInfo.this, tokenManager);

            try {
                JSONObject decodedToken = JWTUtils.decodeJWT(tokenManager.getToken().replace("Bearer ", ""));

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, (new JSONObject()).toString());
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/read/users/info")
                        .post(body)
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody).getJSONObject("message");
                        final String userIdStr = json.getString("id");
                        final String userEmailStr = json.getString("email");
                        final String nickname = json.getString("nickname");

                        runOnUiThread(() -> {
                            textViewNickname.setText(nickname);
                            textViewEmail.setText(userEmailStr);
                            textViewId.setText(userIdStr);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Log.e(tag, "사용자 정보 가져오기 실패");
                        });
                    }
                } catch (IOException | JSONException e) {
                    Log.e(tag, "네트워크 오류", e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tokenManager.clearToken();
                    Toast.makeText(MypageUserInfo.this, "로그인 세션이 만료되어 로그아웃 처리 되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MypageUserInfo.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            }
        }).start();
    }

    // 비번 수정 페이지 클래스
    public static class PasswordBottomSheet extends BottomSheetDialogFragment {
        @Override
        public void onStart() {
            super.onStart();
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            if (dialog != null) {
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                WindowManager wm = getActivity().getWindowManager();
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getRealSize(size);
                int screenHeight = size.y;
                bottomSheet.getLayoutParams().height = screenHeight;
                bottomSheet.setLayoutParams(bottomSheet.getLayoutParams());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setDraggable(false);
            }
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.mypage_modify_pwd, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button btnClosePwd = view.findViewById(R.id.btn_close_password);
            EditText currentPwd = view.findViewById(R.id.edit_pwd_current);
            EditText newPwd = view.findViewById(R.id.edit_pwd_new);
            EditText checkPwd = view.findViewById(R.id.edit_pwd_check);
            Button btnChangePwd = view.findViewById(R.id.changePwd);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnClosePwd.setOnClickListener(v -> dismiss());
            btnChangePwd.setOnClickListener(v -> {
                if (validatePasswords(currentPwd.getText().toString(), newPwd.getText().toString(), checkPwd.getText().toString())) {
                    changePassword(currentPwd.getText().toString(), newPwd.getText().toString());
                } else {
                    Log.i(tag, "비밀번호 확인이 일치하지 않음");
                    Toast.makeText(getContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            // 현 비번 입력한 값이 일치한 지 검사
            currentPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String passwdString = currentPwd.getText().toString();  // 현재 비번
                    checkPwd(passwdString, new CheckPasswordCallback() {
                        @Override
                        public void onResult(String checkCurrentPwd) {
                            if ((passwdString.isEmpty() && charSequence.toString().isEmpty())) {
                                currentPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            } else {
                                if (passwdString.equals(checkCurrentPwd)) {
                                    currentPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                                } else {
                                    currentPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                                }
                            }
                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });

        }

        private void checkPwd(String currentPwd, CheckPasswordCallback callback) {
            new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                TokenManager tokenManager = new TokenManager(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("myInputPasswd", currentPwd);
                    jsonObject.put("newPasswd", "ex");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/update/users/info/passwd")
                        .post(body)
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.i(tag, "서버 응답: " + responseBody);

                    if (response.isSuccessful()) {
                        JSONObject json = new JSONObject(responseBody);
                        String currentPw = json.getString("myInputPasswd");

                        getActivity().runOnUiThread(() -> {
                            Log.i("현재 비번", currentPw);
                            callback.onResult(currentPwd);
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            Log.i("비번 확인 실패", responseBody);
                            Toast.makeText(getContext(), "비밀번호 확인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            callback.onResult(currentPwd);
                        });
                    }
                } catch (IOException | JSONException e) {
                    Log.e(tag, "내부 오류: ", e);
                    getActivity().runOnUiThread(() -> {
                        callback.onResult("");
                    });
                }
            }).start();
        }


        private interface CheckPasswordCallback {
            void onResult(String result);

        }

        private boolean validatePasswords(String current, String newPass, String confirm) {
            Log.i(tag, "비밀번호 변경 시도");
            return newPass.equals(confirm) && !newPass.isEmpty();
        }

        private void changePassword(String currentPwd, String newPwd) {
            new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                TokenManager tokenManager = new TokenManager(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("myInputPasswd", currentPwd);
                    jsonObject.put("newPasswd", newPwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/update/users/info/passwd")
                        .post(body)
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i(tag, "비밀번호 변경 성공");
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            dismiss();
                        });
                    } else {
                        Log.e(tag, "비밀번호 변경 실패");
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    Log.e(tag, "네트워크 오류", e);
                }
            }).start();
        }
    }

    // 닉네임 수정 클래스
    public static class NickNameBottomSheet extends BottomSheetDialogFragment {
        private static final String tag = "닉네임 수정 클래스";
        @Override
        public void onStart() {
            super.onStart();
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            if (dialog != null) {
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                WindowManager wm = getActivity().getWindowManager();
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getRealSize(size);
                int screenHeight = size.y;
                bottomSheet.getLayoutParams().height = screenHeight;
                bottomSheet.setLayoutParams(bottomSheet.getLayoutParams());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setDraggable(false);
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.mypage_modify_nickname, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button btnCloseNickName = view.findViewById(R.id.btn_close_nickname);
            EditText newNickName = view.findViewById(R.id.edit_name_new);
            Button btnCheckNickName = view.findViewById(R.id.btnNicknameCheck);
            Button btnChangeNickName = view.findViewById(R.id.changeNickname);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnCloseNickName.setOnClickListener(v -> dismiss());
            btnCheckNickName.setOnClickListener(v -> checkNickname(newNickName.getText().toString()));
            btnChangeNickName.setOnClickListener(v -> {changeNickname(newNickName.getText().toString());});
        }

        // 닉네임 중복 확인
        private void checkNickname(String nickname) {
            new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                TokenManager tokenManager = new TokenManager(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nickname", nickname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/create/register/check/nickname")
                        .post(body)
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);

                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            int status = response.code();
                            if (status == 400){
                                Toast.makeText(getContext(), (CharSequence) response.body(), Toast.LENGTH_SHORT).show();
                            } else if (status == 409){
                                Toast.makeText(getContext(), "닉네임이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(tag, "서버 오류");                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    Log.e(tag, "내부 오류: ", e);
                }
            }).start();
        }

        // 닉네임 변경 버튼
        private void changeNickname(String newNickname) {
            new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                TokenManager tokenManager = new TokenManager(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("newNickname", newNickname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/update/users/info/nickname")
                        .post(body)
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {

                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "닉네임이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("success nickname change", "success nickname change");
                            textViewNickname.setText(newNickname);
                            dismiss();
                        });
                    } else {
                        int status = response.code();
                        String responseBody = response.body().string();
                        if (status == 404) {
                            Toast.makeText(getContext(), "사용자가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("user not found", "user not found");
                        } else if (status == 400) {
                            Log.e("400", responseBody);
                        } else if (status == 401) {
                            Log.e("Token Error", "Token error");
                        } else {
                            Log.e("server Error", "server Error: "+ responseBody);
                        }
                    }
                } catch (IOException e) {
                   Log.e(tag, "오류", e);
                }
            }).start();
        }
    }
}