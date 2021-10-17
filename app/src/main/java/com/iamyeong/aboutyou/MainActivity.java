package com.iamyeong.aboutyou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;
import android.provider.ContactsContract;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText editText;
    private PersonViewAdapter personViewAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentReference userDocument;
    private CollectionReference appCollection;
    private ImageView fab;
    private FirebaseUser user;

    private LoadingDialog loadingDialog = null;

    private boolean isFirst = true;

    private OnCompleteListener<QuerySnapshot> onCompleteListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    System.out.println("ID" + document.getId());

                    if (document.getId().equals(user.getUid())) isFirst = false;

                }

                loadingDialog.dismiss();
                loadingDialog = null;

                if (isFirst) {
                    addContacts();
                } else {
                    selectPeople();
                }


            } else {
                System.out.println(task.getException());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Light mode or Night mode
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        appCollection = db.collection(getPackageName());
        userDocument = appCollection.document(user.getUid());

        //View id find
        editText = findViewById(R.id.et_search_main);
        fab = findViewById(R.id.fab_main);
        recyclerView = findViewById(R.id.rv_main);

        //Adapter set
        personViewAdapter = new PersonViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(personViewAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
               intent.putExtra("PERSON", new Person());
               startActivity(intent);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                personViewAdapter.filtering(s.toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (user != null) {

            checkFirstAuth(user.getUid());

        }


    }

    private void addContacts() {

        System.out.println("Add" + userDocument.getId());

        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();

        WriteBatch batch = db.batch();

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Map<String, Object> map = new HashMap<>();
            map.put("user_uid", user.getUid());
            map.put("person_name", name);
            map.put("person_number", number);
            //...

            DocumentReference documentReference = userDocument.collection("PEOPLE").document();
            batch.set(documentReference, map);


        }

        Map<String, Object> userField = new HashMap<>();
        userField.put("EXIST", true);
        batch.set(userDocument, userField);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                selectPeople();
            }
        });



    }

    private void selectPeople() {

        System.out.println("Select" + userDocument.getId());

        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();


        userDocument.collection("PEOPLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            personViewAdapter.clearPeople();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Person person = new Person();
                                person.setName(document.get("person_name").toString());
                                person.setPathId(document.getId());
                                personViewAdapter.addPerson(person);
                            }

                            loadingDialog.dismiss();

                        }

                    }
                });




    }

    private void selectUserInfo() {

        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                /*
                user.getMetadata();
                user.getMultiFactor();
                user.getUid();
                user.getDisplayName();
                user.getEmail();
                user.getPhoneNumber();
                user.getPhotoUrl();
                user.getProviderData();
                user.getTenantId();
                user.getIdToken(true);


                 */

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        TwoButtonDialog dialog = new TwoButtonDialog(this, TwoButtonDialog.DIALOG_APP_EXIT);
        dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public void onDialogButtonClick(boolean selectButton) {

                if (selectButton) {
                    finish();
                } else {
                    //Nothing
                }

            }
        });

        dialog.show();

    }

    private void checkFirstAuth(String uid) {

        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();
        System.out.println(appCollection.getId() + ", " + uid);

        appCollection.get().addOnCompleteListener(onCompleteListener);


    }

}