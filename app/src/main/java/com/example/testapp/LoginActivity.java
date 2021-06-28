package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private AppSharePrefManager sharePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharePrefManager = new AppSharePrefManager(this);
        if (sharePrefManager.getState()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }

        EditText usernameEdt = (EditText) findViewById(R.id.username_edt_login);
        EditText passwordEdt = (EditText) findViewById(R.id.password_edt_login);

        TextView dontHaveAccTxt = (TextView) findViewById(R.id.dont_have_acc_txt);
        dontHaveAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        CheckBox showPassCheckBox = (CheckBox) findViewById(R.id.show_password_cbx);
        showPassCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = new ApiService(getApplicationContext());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("personUsernameOB", usernameEdt.getText().toString());
                    jsonObject.put("personPasswordOB", passwordEdt.getText().toString());

                    apiService.loginUser(jsonObject, new ApiService.LoginComplete() {
                        @Override
                        public void login(boolean loginSuccessfully) {
                            if (loginSuccessfully) {
                                sharePrefManager.save(true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } else {
                                sharePrefManager.save(false);

                                Toast.makeText(LoginActivity.this, "نام کاربری یا رمز ورود نامعتبر است", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}