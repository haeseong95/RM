package com.example.rm.mypage;

import android.app.AlertDialog;
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
import com.example.rm.account.SignUp;
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
    //
    private static final String tag = "회원정보 수정";
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
            try {
                OkHttpClient client = ApiClient.getClient(MypageUserInfo.this, tokenManager);
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

    // 비번 수정 클래스
    public static class PasswordBottomSheet extends BottomSheetDialogFragment {
        private static final String tag = "비번 수정 클래스";
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

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String current = currentPwd.getText().toString();
                    String neW = newPwd.getText().toString();
                    String check = checkPwd.getText().toString();

                    if (neW.equals(check) && !neW.isEmpty()) {
                        btnChangePwd.setEnabled(true);
                        btnChangePwd.setBackgroundColor(getContext().getResources().getColor(R.color.main_color_green));
                        btnChangePwd.setTextColor(getContext().getResources().getColor(R.color.white));
                    } else {
                        btnChangePwd.setEnabled(false);
                        btnChangePwd.setBackgroundColor(getContext().getResources().getColor(R.color.category_gray));
                        btnChangePwd.setTextColor(getContext().getResources().getColor(R.color.black));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };
            currentPwd.addTextChangedListener(textWatcher);
            newPwd.addTextChangedListener(textWatcher);
            checkPwd.addTextChangedListener(textWatcher);

            btnChangePwd.setOnClickListener(v -> {changePassword(currentPwd.getText().toString(), newPwd.getText().toString());});  // 새 비번으로 변경
            checkPwd.addTextChangedListener(new TextWatcher() {     // 새 비번이랑 재입력이랑 일치한 지 검사
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String passwdString = newPwd.getText().toString();
                    String passwdChkString = s.toString();

                    if((passwdString.isEmpty() && passwdChkString.isEmpty())){
                        checkPwd.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    } else {
                        if(passwdString.equals(passwdChkString)) {
                            checkPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0 ,R.drawable.check , 0);
                        } else {
                            checkPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0 ,R.drawable.error , 0);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        // myInputPasswd는 currentPwd에 입력된 현재 비번을 해시화해서 db에 저장된 비번과 비교해서 일치하면 newPasswd를 해시화해서 db에 저장/일치x -> 400
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
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.i(tag, "비번 변경 성공");
                            dismiss();
                        });
                    } else {
                        Log.e(tag, "비번 변경 실패");
                        getActivity().runOnUiThread(() -> {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setMessage("현재 비밀번호가 올바르지 않습니다. 다시 입력해주세요.")
                                    .setPositiveButton("확인", (dialog, which) -> dialog.dismiss())
                                    .create();
                            alertDialog.show();
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                            Log.e(tag, "현재 비번 불일치");
                        });
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