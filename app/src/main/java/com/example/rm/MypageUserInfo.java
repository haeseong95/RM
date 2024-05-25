package com.example.rm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
                passwordBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
                passwordBottomSheet.show(getSupportFragmentManager(), "비밀번호 변경");
                break;
            case R.id.btn_modify_nickname:  // 닉네임 수정
                showChangeNickname();
                break;
            case R.id.text_modify_email:    // 이메일 수정
                break;
            default:
                throw new IllegalStateException("버튼 이동 오류남: " + v.getId());
        }
    }

    // 닉네임 수정 dialog 출력
    private void showChangeNickname() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mypage_modify_nickname, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        EditText editNickname = dialogView.findViewById(R.id.edit_nickname);
        Button btnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        Button btnCheckName = dialogView.findViewById(R.id.btn_checkNickName);

        btnCheckName.setOnClickListener(v -> checkNickName());   // 닉네임 중복 확인 버튼
        btnCancel.setOnClickListener(v -> dialog.dismiss());    // 취소 버튼
        btnConfirm.setOnClickListener(v -> updateNickname(editNickname.getText().toString()));  // 확인 버튼
        dialog.show();
    }

    // 닉네임 사용가능한지 검사
    private void checkNickName(){

    }


    // 변경된 닉네임 db에도 적용하기
    private void updateNickname(String newNickname) {
        textViewNickname.setText(newNickname);
    }


    // 비밀번호 변경 창
    public static class PasswordBottomSheet extends BottomSheetDialogFragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.mypage_modify_pwd, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            EditText currentPwd = view.findViewById(R.id.edit_pwd_current);
            EditText newPwd = view.findViewById(R.id.edit_pwd_new);
            EditText checkPwd = view.findViewById(R.id.edit_pwd_check);
            Button btnChangePwd = view.findViewById(R.id.finishBtn);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
            btnChangePwd.setOnClickListener(v -> {
                if (validatePasswords(currentPwd.getText().toString(), newPwd.getText().toString(), checkPwd.getText().toString())) {
                    // 비밀번호 변경 로직 실행, 성공적이면 프래그먼트 닫기
                    dismiss();
                } else {
                    // 사용자에게 오류 메시지 표시
                    Log.i(tag, "비밀번호 확인이 일치하지 않음");
                }
            });
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), getTheme());
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                    FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
                    behavior.setDraggable(false);
                }
            });
            return dialog;
        }


        private boolean validatePasswords(String current, String newPass, String confirm) {
            // 로그는 디버깅을 위해 유지할 수 있지만, 여기서는 실제 비밀번호 검증 필요 + 새 비번이랑 재입력한 게 같은지도 검사
            Log.i(tag, "비밀번호 변경 시도");
            return newPass.equals(confirm) && !newPass.isEmpty();
        }
    }




}