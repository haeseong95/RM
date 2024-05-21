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
