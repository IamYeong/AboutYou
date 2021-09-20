package com.iamyeong.aboutyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private Animation alphaAnimation;
    private TextView title;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        user = (FirebaseUser) intent.getParcelableExtra("USER");

        title = findViewById(R.id.tv_splash);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_default);

        title.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



}