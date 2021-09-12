package com.iamyeong.aboutyou;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private Button googleLoginButton, emailLoginButton;
    private LoginButton facebookLoginButton;
    private Intent intent;

    private static final String GOOGLE_TAG = "com.iamyeong.aboutyou.google.tag";
    private static final String FACEBOOK_TAG = "com.iamyeong.aboutyou.facebook.tag";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    //private CallbackManager callbackManager;

    private ActivityResultCallback<ActivityResult> googleCallback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            firebaseAuthWithGoogle(task.getResult().getIdToken());
        }
    };

    private ActivityResultCallback<ActivityResult> facebookCallback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    };

    private ActivityResultLauncher<Intent> googleResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    googleCallback);

    private ActivityResultLauncher<Intent> facebookResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    facebookCallback
            );

    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.iamyeong.aboutyou", true, "26")
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(LoginActivity.this, SplashActivity.class);
        googleLoginButton = findViewById(R.id.btn_google_login);
        facebookLoginButton = findViewById(R.id.btn_facebook_login);
        emailLoginButton = findViewById(R.id.btn_email_login);

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();


        //logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구글 로그인 시작
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(GOOGLE_TAG)
                        .requestEmail()
                        .build();

                googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

                googleResultLauncher.launch(new Intent(googleSignInClient.getSignInIntent()));


            }
        });

        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Toast.makeText(LoginActivity.this, loginResult.toString(), Toast.LENGTH_SHORT).show();

                        firebaseAuthWithFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(LoginActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {

                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.sendSignInLinkToEmail("email", actionCodeSettings)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    //true : 현재 유저 없음
    private boolean checkCurrentUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            updateUI(firebaseUser);
            return false;
        }

        return true;
    }

    private void updateUI(FirebaseUser user) {

        Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }


    private void firebaseAuthWithFacebook(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });

    }
}