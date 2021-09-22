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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private DocumentReference firestoreDoc;
    private ImageView fab;
    private FirebaseUser user;
    private boolean isFirst;
    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Light mode or Night mode
        setContentView(R.layout.activity_main);

        //계정 가져오기
        Intent intent = getIntent();
        user = (FirebaseUser) intent.getParcelableExtra("USER");
        isFirst = intent.getBooleanExtra("FIRST", false);
        Toast.makeText(this, user.getEmail() + ", " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();
        firestoreDoc = db.collection(getString(R.string.app_package_name)).document(user.getUid());

        //System.out.println(user.getUid());

        //Find id
        editText = findViewById(R.id.et_search_main);
        fab = findViewById(R.id.fab_main);
        recyclerView = findViewById(R.id.rv_main);

        //Adapter set
        personViewAdapter = new PersonViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(personViewAdapter);

        //personViewAdapter.addPerson(new Person());

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

        if (isFirst) {
            isFirst = false;
            addContacts();
        } else {
            selectPeople();
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

            Person person = new Person();
            person.setName(name);
            Map<String, Object> map = new HashMap<>();
            map.put("user_uid", user.getUid());
            map.put("person_name", name);
            map.put("person_number", number);
            //...


            firestoreDoc.collection("PEOPLE").add(map);


        }
        selectPeople();

    }

    private void selectPeople() {

        //Firestore uid 에 해당하는 친구리스트를 객체로 파싱하여 가져올 것.
        //메모까지 다 가져와야 하는데 속도를 어떻게 개선할 것인가.
        //그냥 따로따로 해보자! 여기서는 친구 리스트만 조회! InfoActivity 에서는 그 친구에 해당하는 메모리스트만 조회!
        //유니크한 문서의 아이디별로 Person 객체 리스트를 만들어놓으면,
        //특정 지인을 클릭헀을 때, 아이디로 콜렉션을 찾고, 프로필과 메모를 조회,
        //UI 갱신가능

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(MainActivity.this);
            loadingDialog.show();
        }

        firestoreDoc.collection("PEOPLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot doc : documents) {

                                Person person = new Person();
                                person.setName(doc.get("person_name").toString());

                                personViewAdapter.addPerson(person);

                            }

                        }

                    }
                });


        loadingDialog.dismiss();

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