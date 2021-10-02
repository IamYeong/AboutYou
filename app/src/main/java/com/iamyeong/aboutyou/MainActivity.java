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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;
import android.provider.ContactsContract;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText editText;
    private PersonViewAdapter personViewAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentReference firestoreDocument;
    private CollectionReference firestoreCollection;
    private ImageView fab;
    private FirebaseUser user;

    private LoadingDialog loadingDialog = null;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Light mode or Night mode
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        firestoreCollection = db.collection(getPackageName());

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

        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();

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


            firestoreDocument.collection("PEOPLE").add(map);


        }
        loadingDialog.dismiss();
        selectPeople();

    }

    private void selectPeople() {


        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();


        firestoreDocument.collection("PEOPLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            personViewAdapter.clearPeople();
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot doc : documents) {

                                Person person = new Person();
                                person.setName(doc.get("person_name").toString());
                                person.setPathId(doc.getId());
                                personViewAdapter.addPerson(person);

                            }

                            loadingDialog.dismiss();

                        }

                    }
                });




    }

    private void selectUserInfo() {

        firestoreDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        LoadingDialog dialog = new LoadingDialog(MainActivity.this);
        dialog.show();

        firestoreCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot doc : documents) {

                            if (doc.getId().equals(uid)) {
                                isFirst = false;
                            }

                        }

                        firestoreDocument = firestoreCollection.document(uid);

                        if (isFirst) {
                            addContacts();
                        } else {
                            selectPeople();
                        }
                        dialog.dismiss();

                    }
                });

    }

}