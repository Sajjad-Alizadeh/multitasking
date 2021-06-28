package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText nameEdt;
    private EditText emailEdt;
    private EditText usernameEdt;
    private EditText passwordEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        nameEdt = (EditText) findViewById(R.id.name_edt);
        emailEdt = (EditText) findViewById(R.id.email_edt);
        usernameEdt = (EditText) findViewById(R.id.user_name_edt);
        passwordEdt = (EditText) findViewById(R.id.password_edt);

        TextView txtHaveAcc = (TextView) findViewById(R.id.txt_have_acc);
        txtHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        Button signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = new ApiService(SignUpActivity.this);
                if (!checkEditTextNotNull()) {
                    JSONObject user = new JSONObject();
                    try {
                        user.put("nameOB", nameEdt.getText().toString());
                        user.put("emailOB", emailEdt.getText().toString());
                        user.put("usernameOB", usernameEdt.getText().toString());
                        user.put("passwordOB", passwordEdt.getText().toString());
                        apiService.saveUser(user, new ApiService.UserSaved() {
                            @Override
                            public void saved(boolean success) {
                                if (success) {
                                    Toast.makeText(SignUpActivity.this, "کاربر با موفقیت ایجاد شد", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "خطا در برقراری با سرور", Toast.LENGTH_LONG).show();
                                }
                            }


                        });
                    } catch (JSONException e) {
                        Log.e(TAG, "onClick: " + e.getMessage());
                    }

                }


            }
        });


    }


    private boolean checkEditTextNotNull() {

        if (!nameEdt.getText().toString().isEmpty() && !emailEdt.getText().toString().isEmpty() && !usernameEdt.getText().toString().isEmpty() && !passwordEdt.getText().toString().isEmpty()) {
            return false;
        } else {
            Toast.makeText(this, "لطفا مقادیر خواسته شده را پر کنید", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nameEdt.setText("");
        emailEdt.setText("");
        usernameEdt.setText("");
        passwordEdt.setText("");
    }

}