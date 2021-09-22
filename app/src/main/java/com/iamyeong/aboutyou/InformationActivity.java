package com.iamyeong.aboutyou;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.iamyeong.aboutyou.dto.Memo;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.dto.User;
import com.iamyeong.aboutyou.listener.OnFragmentDataNotifyListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationActivity extends AppCompatActivity implements OnFragmentDataNotifyListener<Memo> {

    private RecyclerView recyclerView;
    private MemoViewAdapter memoViewAdapter;
    private Toolbar toolbar;
    private TextView sortText, nameText, ageText, groupText, descText;
    private EditText editText;
    private FloatingActionButton fab;
    private boolean sortAscending = true;
    private ImageView modifyImage;
    private Person person;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private MemoFragment fragment;
    private DocumentReference document;
    private int sortFlag = 0;
    private int searchFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //정보 받기
        Intent intent = getIntent();
        person = (Person)intent.getSerializableExtra("PERSON");
        user = (FirebaseUser) intent.getSerializableExtra("USER");
        db = FirebaseFirestore.getInstance();
        document = db.collection(getString(R.string.app_package_name))
                .document(user.getUid()).collection("PEOPLE")
                .document(person.getPathId());

        //view id set
        allFindViewById();

        //어댑터 세팅
        memoViewAdapter = new MemoViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memoViewAdapter);

        //리스너 설정
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

                //searchFlag를 조작하는 UI를 추가하고
                //제목검색 / 내용검색 / 태그검색 / 기간검색 추가할 것.

                memoViewAdapter.findMemoByTitle(s.toString());

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fragment = new MemoFragment();
                transaction.addToBackStack(null);

                if (fragment.isAdded()) {
                    transaction.remove(fragment);
                }

                transaction.add(R.id.info_container, fragment).commit();

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

        //정보 셋팅
        updateProfile();

        //메모 불러오기
        selectMemos();

    }

    private void addMemo(Memo memo) {

        Map<String, Object> map = new HashMap<>();
        map.put("document_id", memo.getDocumentId());
        map.put("memo_title", memo.getTitle());
        map.put("memo_content", memo.getContent());

        document.collection("MEMO")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        selectMemos();
                    }
                });

    }

    private void selectMemos() {

        document.collection("MEMO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //문서가 비어있지 않다면
                        if (task.getResult().getDocuments().size() != 0 && task.getResult().getDocuments() != null) {

                            LoadingDialog dialog = new LoadingDialog(InformationActivity.this);
                            dialog.show();

                            memoViewAdapter.clearMemos();

                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                Memo memo = new Memo();
                                memo.setTitle(doc.get("memo_title").toString());
                                memo.setContent(doc.get("memo_content").toString());
                                memo.setDocumentId(doc.getId());

                                memoViewAdapter.addMemo(memo);
                            }

                            //정렬

                            dialog.dismiss();

                        }

                    }
                });

    }

    private void updateMemo(Memo memo) {

        Map<String, Object> map = new HashMap<>();
        map.put("document_id", memo.getDocumentId());
        map.put("memo_title", memo.getTitle());
        map.put("memo_content", memo.getContent());


        document.collection("MEMO")
                .document(memo.getDocumentId())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        selectMemos();
                    }
                });

    }

    private void deleteMemo(Memo memo) {

        document.collection("MEMO")
                .document(memo.getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        selectMemos();
                    }
                });

    }

    private void updateProfile() {

        nameText.setText(person.getName());

    }

    private void sortMemos() {
        //이 코드를 어댑터로 옮길 수도 있음.

        switch(sortFlag) {
            case 0 :
                //날짜 최신순(내림차순)
                break;

            case 1 :
                //날짜 오래된순(오름차순)
                break;

            case 2 :
                //수정날짜 최신순
                break;


        }


    }

    private void allFindViewById() {
        toolbar = findViewById(R.id.toolbar_information);
        sortText = findViewById(R.id.tv_sort_memo);
        editText = findViewById(R.id.et_search_memo);
        modifyImage = findViewById(R.id.img_modify_info);

        recyclerView = findViewById(R.id.rv_info);

        nameText = findViewById(R.id.tv_name_profile);
        ageText = findViewById(R.id.tv_age_profile);
        descText = findViewById(R.id.tv_description_profile);
        groupText = findViewById(R.id.tv_group_profile);

        fab = findViewById(R.id.fab_memo);
    }

    @Override
    public void onFragmentListener(Memo memo) {

        if (fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment);
        }

        if (memo.getDocumentId() == null) {
            addMemo(memo);
        } else {
            updateMemo(memo);
        }

        //완료되면 로딩 닫고 프래그먼트 제거.

    }
}