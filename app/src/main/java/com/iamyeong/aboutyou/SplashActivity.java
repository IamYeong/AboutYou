package com.iamyeong.aboutyou;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private Animation alphaAnimation;
    private TextView title;
    private FirebaseUser user;
    private boolean isFirst;

    //Permission 허용할 것
    private String[] permissions = new String[] {
            Manifest.permission.READ_CONTACTS
    };

    private List<String> deniedPermissions = new ArrayList<>();

    private ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {

                            for (String permission : result.keySet()) {

                                //해당 권한이 미동의 상태라면?
                                if (!result.get(permission)) {
                                    deniedPermissions.add(permission);
                                }

                            }

                            if (deniedPermissions.isEmpty()) {
                                startMainActivity();
                            } else {
                                Toast.makeText(SplashActivity.this, deniedPermissions.size() + "개의 권한 미동의", Toast.LENGTH_SHORT).show();
                            }



                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        user = (FirebaseUser) intent.getParcelableExtra("USER");
        isFirst = intent.getBooleanExtra("FIRST", false);

        title = findViewById(R.id.tv_splash);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_default);

        title.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

               activityResultLauncher.launch(permissions);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("USER", user);
        intent.putExtra("FIRST", isFirst);
        startActivity(intent);
        finish();
    }


}