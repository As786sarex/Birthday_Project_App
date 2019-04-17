package com.wildcardenter.myfab.for_jahan.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.wildcardenter.myfab.for_jahan.R;

import es.dmoral.toasty.Toasty;


public class WelcomeActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        lottieAnimationView=findViewById(R.id.lottieAniView);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setSpeed(.8f);
        TextView animatedTextView=findViewById(R.id.welcome_animated_text);
        animatedTextView.setTypeface(Typeface.createFromAsset(this.getAssets(),"fonts/HORNI___.TTF"));
        animatedTextView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_from_left));

        new Handler().postDelayed(() -> {
            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        },2000);

    }
}
