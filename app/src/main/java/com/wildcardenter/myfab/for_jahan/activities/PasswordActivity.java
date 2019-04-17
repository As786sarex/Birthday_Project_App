package com.wildcardenter.myfab.for_jahan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;
import com.wildcardenter.myfab.for_jahan.R;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        PasscodeView passcodeView = findViewById(R.id.passcodeView);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MySharedData", MODE_PRIVATE);


        passcodeView.setLocalPasscode(preferences.getString("myKey", "1704"));
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                passcodeView.setWrongInputTip("Get the Password From Asif");
            }

            @Override
            public void onSuccess(String number) {

                startActivity(new Intent(PasswordActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }
}
