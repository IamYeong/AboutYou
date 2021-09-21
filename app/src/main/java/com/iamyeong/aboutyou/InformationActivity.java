package com.iamyeong.aboutyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dto.Memo;
import com.iamyeong.aboutyou.dto.Person;

public class InformationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemoViewAdapter memoViewAdapter;
    private Toolbar toolbar;
    private TextView sortText;
    private EditText editText;
    private boolean sortAscending = true;
    private ImageView modifyImage;
    private Person person;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        person = (Person)intent.getSerializableExtra("PERSON");

        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar_information);
        sortText = findViewById(R.id.tv_sort_memo);
        editText = findViewById(R.id.et_search_memo);
        modifyImage = findViewById(R.id.img_modify_info);

        recyclerView = findViewById(R.id.rv_info);
        memoViewAdapter = new MemoViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memoViewAdapter);

        memoViewAdapter.addMemo(new Memo());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortAscending) {
                    memoViewAdapter.sortAscendingOrder();
                } else {
                    memoViewAdapter.sortDescendingOrder();
                }
            }
        });

        modifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputFragment fragment = new InputFragment(person, InputFragment.PERSON_UPDATE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (fragment.isAdded()) {
                    transaction.remove(fragment);
                }

                transaction.add(R.id.info_container, fragment).commit();

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
                memoViewAdapter.findMemoByTitle(s.toString());
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

        //불러오기

    }

    private void selectMemos() {



    }

    private void updateMemo() {

    }

    private void deleteMemo() {

    }

    private void updateProfile() {

    }

}