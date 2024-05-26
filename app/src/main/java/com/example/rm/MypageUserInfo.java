package com.example.rm;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// 회원 정보 수정 페이지
public class MypageUserInfo extends AppCompatActivity implements View.OnClickListener{
    // 레이아웃
    ImageView imageView;
    EditText editTextId;
    Button btnPwd, btnNickname, btnEmail;
    TextView textViewNickname, textViewEmail;

    //
    private static final String tag = "회원정보 수정, MypageUserInfo";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modify_userinfo);
        imageView = findViewById(R.id.btn_back);
        editTextId = findViewById(R.id.text_modify_id);
        btnPwd = findViewById(R.id.btn_modify_password);
        btnNickname = findViewById(R.id.btn_modify_nickname);
        btnEmail = findViewById(R.id.btn_modify_email);
        textViewNickname = findViewById(R.id.text_modify_nickname);
        textViewEmail = findViewById(R.id.text_modify_email);

        imageView.setOnClickListener(this);
        btnPwd.setOnClickListener(this);
        btnNickname.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        editTextId.setClickable(false);
        editTextId.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.text_modify_email:    // 이메일 수정
                EmailBottomSheet emailBottomSheet = new EmailBottomSheet();
                emailBottomSheet.show(getSupportFragmentManager(), "이메일 변경");
                break;
            default:
                throw new IllegalStateException("버튼 이동 오류남: " + v.getId());
        }
    }

    // 비밀번호 변경 클래스
    public static class PasswordBottomSheet extends BottomSheetDialogFragment{
        // bottom dialog 높이 설정
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

                bottomSheet.getLayoutParams().height = screenHeight;    // Bottom Sheet의 높이를 화면 높이가 꽉 차게 함 + 드래그 안됨
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
            EditText currentPwd = view.findViewById(R.id.edit_pwd_current); // 현 비번이 맞는지 체크
            EditText newPwd = view.findViewById(R.id.edit_pwd_new); // 새 비번 입력
            EditText checkPwd = view.findViewById(R.id.edit_pwd_check); // 새 비번이랑 맞는지 체크
            Button btnChangePwd = view.findViewById(R.id.changePwd);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnClosePwd.setOnClickListener(v -> dismiss());
            btnChangePwd.setOnClickListener(v -> {
                if (validatePasswords(currentPwd.getText().toString(), newPwd.getText().toString(), checkPwd.getText().toString())) {
                    // 비밀번호 변경 실행, 성공적이면 프래그먼트 닫기
                    dismiss();
                } else {
                    // 사용자에게 오류 메시지 표시
                    Log.i(tag, "비밀번호 확인이 일치하지 않음");
                }
            });
        }

        private boolean validatePasswords(String current, String newPass, String confirm) {
            // 실제 비밀번호 검증 필요 + 새 비번이랑 재입력한 게 같은지도 검사
            Log.i(tag, "비밀번호 변경 시도");
            return newPass.equals(confirm) && !newPass.isEmpty();
        }
    }

    // 닉네임 변경 클래스
    public static class NickNameBottomSheet extends BottomSheetDialogFragment{
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
            return inflater.inflate(R.layout.mypage_modify_pwd, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button btnCloseNickName = view.findViewById(R.id.btn_close_nickname);
            EditText newNickName = view.findViewById(R.id.edit_name_new);   // 새 닉네임 입력
            Button btnCheckNickName = view.findViewById(R.id.btnNicknameCheck);
            Button btnChangeNickName = view.findViewById(R.id.changeNickname);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnCloseNickName.setOnClickListener(v -> dismiss());
            btnCheckNickName.setOnClickListener(v -> {
                // 새 닉네임이 중복되는 닉네임 중복 확인
            });
            btnChangeNickName.setOnClickListener(v -> {
                // 서버에 변경된 닉네임 이름 넣고, 메인 화면에 textview 바꾸기
            });
        }
    }

    // 이메일 변경 클래스
    public static class EmailBottomSheet extends BottomSheetDialogFragment{
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
            return inflater.inflate(R.layout.mypage_modify_pwd, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button btnCloseEmail = view.findViewById(R.id.btn_close_email);
            EditText newEmail = view.findViewById(R.id.edit_email_new); // 새 이메일 입력
            Button btnCheckEmail = view.findViewById(R.id.emailCheckButton);    // 새 이메일로 인증번호 보냄
            EditText checkEmailVerify = view.findViewById(R.id.edit_email_verify);  // 이메일로 보낸 인증번호 입력
            Button btnCheckEmailVerify = view.findViewById(R.id.btn_email_verify);  // 인증번호 확인
            Button btnChangeEmail = view.findViewById(R.id.btn_change_email);   //이메일 변경 버튼
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnCloseEmail.setOnClickListener(v -> dismiss());
            btnCheckEmail.setOnClickListener(v -> {
                // 이메일로 인증번호 보냄
            });

            btnCheckEmailVerify.setOnClickListener(v -> {
                // 인증번호 확인 버튼
            });

            btnChangeEmail.setOnClickListener(v -> {
                // 이메일 변경 성공
            });
        }
    }
}