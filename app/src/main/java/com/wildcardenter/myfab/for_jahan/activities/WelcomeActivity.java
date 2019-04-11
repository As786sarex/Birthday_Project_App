package com.wildcardenter.myfab.for_jahan.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.wildcardenter.myfab.for_jahan.R;


public class WelcomeActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        lottieAnimationView=findViewById(R.id.lottieAniView);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setSpeed(.8f);

        new Handler().postDelayed(() -> {
            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        },2000);

    }
}
