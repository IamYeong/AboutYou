package com.iamyeong.aboutyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText editText;
    private PersonViewAdapter personViewAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView fab;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Light mode or Night mode
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = (FirebaseUser) intent.getParcelableExtra("USER");

        Toast.makeText(this, user.getEmail() + ", " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

        editText = findViewById(R.id.et_search_main);
        fab = findViewById(R.id.fab_main);
        recyclerView = findViewById(R.id.rv_main);
        personViewAdapter = new PersonViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(personViewAdapter);

        personViewAdapter.addPerson(new Person());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputFragment fragment = new InputFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (fragment.isAdded()) {
                    transaction.remove(fragment);
                }

                transaction.add(R.id.main_container, fragment);
                transaction.commit();


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
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();


        /*
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("dddd", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dd", "Error adding document", e);
                    }
                });

         */
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
}