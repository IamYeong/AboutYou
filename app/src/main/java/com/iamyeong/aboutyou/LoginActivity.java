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
import android.view.ViewGroup;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, pwEdit;
    private Button emailLoginButton, googleLoginButton;
    private LoginButton facebookLoginButton;
    private FirebaseAuth mAuth;
    private boolean isSuccess = false;
    private FirebaseFirestore db;
    private CollectionReference firestoreCollection;

    //?????? ????????? ?????? ??????
    private ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    signGoogleAccount(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestoreCollection = db.collection(getPackageName());

        //FindViewById
        emailEdit = findViewById(R.id.et_email_input);
        pwEdit = findViewById(R.id.et_email_pw_input);
        emailLoginButton = findViewById(R.id.btn_email_login);
        googleLoginButton = findViewById(R.id.btn_google_login);
        facebookLoginButton = findViewById(R.id.btn_facebook_login);
        pwEdit = findViewById(R.id.et_email_pw_input);

        emailEdit.setText(SharedPreferencesManager.getPreviousEmail(LoginActivity.this, "PRE_EMAIL"));

        //View ?????? ????????? ??????
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEdit.getText().toString();
                String password = pwEdit.getText().toString();

                SharedPreferencesManager.setPreviousEmail(LoginActivity.this, "PRE_EMAIL", email);

                if (email.length() != 0) {
                    signEmailAccount(email, password);
                } else {
                    System.out.println("???????????? ??????????????????!");
                }

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

        //???????????? ????????? ?????????
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

                System.out.println("Facebook cancel");
            }

            @Override
            public void onError(FacebookException error) {

                System.out.println("Facebook Error");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //?????? ?????? ????????? ????????? true ??????, ????????? false ??????
    private boolean checkCurrentUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return false;
        } else {
            return true;
        }

    }

    //????????? ?????? ??????.
    private void createEmailAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startSplash();

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

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //??????????????? ??????????????? ??????????????????.
                        TwoButtonDialog dialog = new TwoButtonDialog(LoginActivity.this, TwoButtonDialog.DIALOG_EMAIL_ACCOUNT);
                        dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public void onDialogButtonClick(boolean selectButton) {
                                if (selectButton) {
                                    createEmailAccount(email, password);
                                }

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                })

                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {

                        System.out.println("on success : " + authResult.getUser().getEmail());
                        isSuccess = true;

                    }
                })

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (isSuccess) startSplash();

                    }
                });

    }


    //????????? ?????? ??????????????? ????????? ??? ?????? ????????? or ?????? ??????
    private void signGoogleAccount(ActivityResult result) {

        //result.getResultCode()

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        AuthCredential credential = GoogleAuthProvider.getCredential(task.getResult().getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("?????? ????????? ??????");
                    }
                })

                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {
                        isSuccess = true;
                    }
                })

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (isSuccess) startSplash();
                    }
                });
    }

    private void signFacebookAccount(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("?????? ????????? ??????");
                    }
                })

                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {
                        isSuccess = true;
                    }
                })

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //SharedPreferencesManager.setPreviousEmail(LoginActivity.this, "PRE_EMAIL", )
                        if (isSuccess) startSplash();
                    }
                });

    }


    private void logout() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void startSplash() {

        //????????? ????????? ????????? ??????

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            System.out.println("User is null");
        }

    }




}