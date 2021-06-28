package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.InfoModel;

public class AddMemoActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    public static List<InfoModel> memos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);


        EditText memoTitle = (EditText) findViewById(R.id.memo_title_edt);
        EditText memoContent = (EditText) findViewById(R.id.memo_content_edt);
        Button saveMemoBtn = (Button) findViewById(R.id.save_memo_btn);
        Button cancelMemoBtn = (Button) findViewById(R.id.cancel_memo_btn);

        memoTitle.setText(getIntent().getStringExtra(TITLE));
        memoContent.setText(getIntent().getStringExtra(CONTENT));

        saveMemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!memoTitle.getText().toString().isEmpty() && !memoTitle.getText().toString().isEmpty()) {
                    MyDatabase myDatabase = new MyDatabase(AddMemoActivity.this);
                    InfoModel memo = new InfoModel();
                    memo.setTitle(memoTitle.getText().toString());
                    memo.setContent(memoContent.getText().toString());
                    myDatabase.addMemo(memo);
                    startActivity(new Intent(AddMemoActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });


        cancelMemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AddMemoActivity.this, MainActivity.class));
                finish();
            }
        });


    }
}