package com.example.rm.manager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

public class NNoticeEdit extends AppCompatActivity {
    ImageView imageView;
    EditText editTitle, editContent;
    TextView btnDelete, btnCreate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_notice_edit);
        imageView = findViewById(R.id.btn_back);
        editTitle = findViewById(R.id.manager_editTextTitle);
        editContent = findViewById(R.id.manager_editTextContent);
        btnDelete = findViewById(R.id.btn_manager_postdelete);
        btnCreate = findViewById(R.id.btn_manager_postcreate);

    }




}
