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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, pwEdit;
    private Button emailLoginButton, googleLoginButton;
    private LoginButton facebookLoginButton;
    private FirebaseAuth mAuth;
    private boolean isFirst = false;
    //private ActionCodeSettings actionCodeSettings;
    //private Uri deepLink;

    //private FireStoreManager fireStoreManager;

    private ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    signGoogleAccount(result);
                }
            }
    );

    //private ActivityResultCallback<ActivityResult> activityResultCallback;

    private static final String GOOGLE_TAG = "com.iamyeong.aboutyou.google.tag";
    private static final String FACEBOOK_TAG = "com.iamyeong.aboutyou.facebook.tag";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //FindViewById
        emailEdit = findViewById(R.id.et_email_input);
        pwEdit = findViewById(R.id.et_email_pw_input);
        emailLoginButton = findViewById(R.id.btn_email_login);
        googleLoginButton = findViewById(R.id.btn_google_login);
        facebookLoginButton = findViewById(R.id.btn_facebook_login);
        pwEdit = findViewById(R.id.et_email_pw_input);

        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEdit.getText().toString();
                String password = pwEdit.getText().toString();

                signEmailAccount(email, password);



            }
        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient client = GoogleSignIn.getClient(LoginActivity.this, gso);

                googleLauncher.launch(new Intent(client.getSignInIntent()));


            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookLoginButton.setReadPermissions("", "");
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                signFacebookAccount(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //현재 유저 정보가 있으면 true 반환, 없으면 false 반환
    private boolean checkCurrentUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return false;
        } else {
            return true;
        }

    }

    private void createEmailAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startSplash(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }

    private void signEmailAccount(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startSplash(user);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                            TwoButtonDialog dialog = new TwoButtonDialog(LoginActivity.this, TwoButtonDialog.DIALOG_EMAIL_ACCOUNT);
                            dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                                @Override
                                public void onDialogButtonClick(boolean selectButton) {
                                    if (selectButton) {
                                        isFirst = true;
                                        createEmailAccount(email, password);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            dialog.show();

                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    //결과에 따라 유저정보를 저장한 뒤 바로 로그인 or 계정 생성
    private void signGoogleAccount(ActivityResult result) {

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

        AuthCredential credential = GoogleAuthProvider.getCredential(task.getResult().getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            startSplash(task.getResult().getUser());
                            //Toast.makeText(LoginActivity.this, "Facebook!", Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }

    private void signFacebookAccount(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            startSplash(task.getResult().getUser());
                            //Toast.makeText(LoginActivity.this, "Facebook!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    private void startSplash(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        intent.putExtra("USER", user);
        intent.putExtra("FIRST", isFirst);
        startActivity(intent);
        finish();
    }



}