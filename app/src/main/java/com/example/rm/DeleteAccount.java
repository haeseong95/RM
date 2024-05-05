package com.example.rm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteAccount extends AppCompatActivity {

    EditText editPw;
    ImageView btnBack;
    Button btnDelete;
    String userPw = null;
    SqliteHelper sqliteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_account);

        btnBack = findViewById(R.id.btn_back);
        editPw = findViewById(R.id.edit_pw);
        btnDelete = findViewById(R.id.btn_delete);
        sqliteHelper = new SqliteHelper(this);
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccount.this);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(DeleteAccount.this);
            AlertDialog alertDialog;
            @Override
            public void onClick(View v) {
                userPw = editPw.getText().toString().trim();
                String id = PreferenceHelper.getLoginId(DeleteAccount.this);
                String hashPw = SignUp.getSHA(userPw);  // 입력한 비번을 해시로 바꾸기
                Log.i("DeleteAccount 계정 조회", "아이디: " + id + ", 비번: " + userPw + ", 해시값 : " + hashPw);

                if (sqliteHelper.checkIdPassword(id, hashPw)){
                    builder.setTitle("탈퇴하기");
                    builder.setMessage("정말 탈퇴하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 비번 일치하면 계정 삭제
                            PreferenceHelper.logout();
                            sqliteHelper.delAccount(id);
                            finishDelete();
                        }
                    });

                    builder.setNeutralButton("아니오", (dialog, which) -> dialog.dismiss());
                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else {  // 비번 잘못 입력함
                    Log.e("DeleteAccount 비번 불일치", "아이디: " + id + ", 비번: " + userPw + ", 해시값 : " + hashPw);
                    builder2.setTitle("비밀번호 오류");
                    builder2.setMessage("입력하신 비밀번호가 정확하지 않습니다. 다시 시도해 주세요.");
                    builder2.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                    alertDialog = builder2.create();
                    alertDialog.show();
                }
            }
        });
    }



    private void finishDelete(){
        AlertDialog.Builder secondBuilder = new AlertDialog.Builder(DeleteAccount.this);
        secondBuilder.setTitle("탈퇴 완료");
        secondBuilder.setMessage("탈퇴 처리가 성공적으로 완료되었습니다.");
        secondBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(DeleteAccount.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        AlertDialog secondDialog = secondBuilder.create();
        secondDialog.show();
    }

}
