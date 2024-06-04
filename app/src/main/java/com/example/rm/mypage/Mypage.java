package com.example.rm.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.token.ApiClient;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    // 레이아웃
    LinearLayout btnLogout, btnDelete, btnCommunity, btnUserInfo;
    ImageView btnBack;
    TextView userId, userEmail;

    //
    private static final String tag = "마이페이지";
    TokenManager tokenManager;
    private static final String USER_INFO_URL = "http://ipark4.duckdns.org:58395/api/read/users/info";  // Flask 서버의 유저 정보 URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        userId = findViewById(R.id.user_id);
        userEmail = findViewById(R.id.user_email);
        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);
        btnDelete = findViewById(R.id.li_delete);
        btnCommunity = findViewById(R.id.li_community);
        btnUserInfo = findViewById(R.id.li_user);

        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCommunity.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        tokenManager = new TokenManager(this);

        fetchUserInfo();
    }

    // 마이페이지 상단에 닉네임 표시 + 토큰 만료되면 자동 로그아웃
    private void fetchUserInfo() {
        new Thread(() -> {
            OkHttpClient client = ApiClient.getClient(Mypage.this, tokenManager);

            try {
                Request request = new Request.Builder()
                        .url(USER_INFO_URL)
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
                            userId.setText(nickname);
                            userEmail.setText(userEmailStr);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Log.e(tag, "사용자 정보 가져오기 실패: " + response.message());
                            Toast.makeText(Mypage.this, "사용자 정보 가져오기 실패", Toast.LENGTH_SHORT).show();
                            if (response.code() == 401) {
                                tokenManager.clearToken();
                                Intent intent = new Intent(Mypage.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    Log.e(tag, "네트워크 오류", e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tokenManager.clearToken();
                    Toast.makeText(Mypage.this, "로그인 세션이 만료되어 로그아웃 처리 되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Mypage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_logout:   // 로그아웃
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", (dialog, which) -> {
                    PreferenceHelper.logout();
                    tokenManager.clearToken();
                    Intent intent = new Intent(Mypage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(Mypage.this, "로그아웃 하였습니다.", Toast.LENGTH_LONG).show();
                });
                builder.setNeutralButton("취소", (dialog, which) -> dialog.dismiss());
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.li_delete:    // 탈퇴하기 페이지
                DeleteAccountBottomSheet sheet = new DeleteAccountBottomSheet(Mypage.this);
                sheet.show(getSupportFragmentManager(), "계정 탈퇴하기");
                break;
            case R.id.li_community: // 내 게시글/댓글 수정 페이지
                Intent intent = new Intent(Mypage.this, MypageModify.class);
                startActivity(intent);
                break;
            case R.id.li_user:  // 회원 정보 수정 페이지
                Intent intent1 = new Intent(Mypage.this, MypageUserInfo.class);
                startActivity(intent1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    // 탈퇴하기
    public static class DeleteAccountBottomSheet extends BottomSheetDialogFragment {
        private Context context;
        public DeleteAccountBottomSheet(Context context){
            this.context = context;
        }

        @Override
        public void onStart() {
            super.onStart();
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            if (dialog != null) {
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
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
            return inflater.inflate(R.layout.mypage_delete_account, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button btnCloseDelete = view.findViewById(R.id.btn_close_delete);
            EditText deleteCheckPwd = view.findViewById(R.id.delete_edit_pwd); // 현 비번이 맞으면 탈퇴시킴
            Button btnDeleteAccount = view.findViewById(R.id.changePwd);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnCloseDelete.setOnClickListener(v -> dismiss());
            btnDeleteAccount.setOnClickListener(v -> {  // 비번 맞는지 검사한 뒤에 탈퇴할거냐는 메시지 띄우기
                String pwd = deleteCheckPwd.getText().toString();
                validatePassword(pwd);
            });
        }

        private void validatePassword(String checkPwd) {
            // 비밀번호 검증 로직, 서버에 검증 요청 후 결과에 따라 confirmDeletionDialog 호출
            checkDelete();
        }

        private void checkDelete() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("계정 탈퇴");
            builder.setMessage("계정을 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.");
            builder.setPositiveButton("확인", (dialog, which) -> finishDelete());
            builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            AlertDialog confirmDialog = builder.create();
            confirmDialog.show();
        }

        private void finishDelete() {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(getActivity(), "계정이 성공적으로 탈퇴 처리되었습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
