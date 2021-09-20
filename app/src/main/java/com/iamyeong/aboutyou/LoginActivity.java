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
    private Button emailLoginButton, googleLoginButton, facebookLoginButton;
    private FirebaseAuth mAuth;
    //private ActionCodeSettings actionCodeSettings;
    //private Uri deepLink;

    private FireStoreManager fireStoreManager;

    private ActivityResultLauncher<Intent> resultLauncher;
    private ActivityResultCallback<ActivityResult> activityResultCallback;

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

                activityResultCallback = new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                };




            }
        });

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityResultCallback = new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                };

            }
        });




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
    private void signGoogleAccount() {

    }

    private void signFacebookAccount() {

    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    private void activityResultRequest(ActivityResultCallback<ActivityResult> callback) {

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback
        );

        //resultLauncher.launch();

    }


}