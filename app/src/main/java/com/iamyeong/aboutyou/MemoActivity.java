package com.iamyeong.aboutyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Memo;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

public class MemoActivity extends AppCompatActivity {

    private EditText title, content;
    private Memo memo;
    private Person person;
    private DocumentReference document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Button confirmButton;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        memo = (Memo) intent.getSerializableExtra("MEMO");
        person = (Person) intent.getSerializableExtra("PERSON");

        document = db.collection(getString(R.string.app_package_name))
                .document(user.getUid()).collection("PEOPLE")
                .document(person.getPathId());

        title = findViewById(R.id.et_title_memo_fragment);
        content = findViewById(R.id.et_content_memo_fragment);
        confirmButton = findViewById(R.id.btn_memo_fragment_confirm);

        title.setText(memo.getTitle());
        content.setText(memo.getContent());
        memo.setDocumentId(null);

        Toolbar toolbar = findViewById(R.id.toolbar_memo);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwoButtonDialog dialog = new TwoButtonDialog(MemoActivity.this, TwoButtonDialog.DIALOG_FINISH_MEMO);
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(boolean selectButton) {

                        dialog.dismiss();
                        if (selectButton) {

                            finish();
                            //removeFragment();
                        }

                    }
                });
                dialog.show();

            }
        });

        ImageView deleteImage = findViewById(R.id.img_delete_memo);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwoButtonDialog dialog = new TwoButtonDialog(MemoActivity.this, TwoButtonDialog.DIALOG_DELETE_MEMO);
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(boolean selectButton) {

                        if (selectButton) {
                            deleteMemo(memo);
                        }

                        dialog.dismiss();
                    }
                });

                dialog.show();



            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (memo.getDocumentId().isEmpty()) {

                    Memo memo = new Memo();


                    addMemo(memo);
                } else {
                    updateMemo();
                }

            }
        });


    }

    private void updateMemo() {

        String titleChar = title.getText().toString();
        String contentChar = content.getText().toString();
        //날짜

        memo.setTitle(titleChar);
        memo.setContent(contentChar);

    }

    private void createMemo() {

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
                        //selectMemos();
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
                        finish();
                    }
                });

    }

    private void deleteMemo(Memo memo) {

        TwoButtonDialog dialog = new TwoButtonDialog(MemoActivity.this, TwoButtonDialog.DIALOG_DELETE_MEMO);
        dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public void onDialogButtonClick(boolean selectButton) {

                if (selectButton) {

                    document.collection("MEMO")
                            .document(memo.getDocumentId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                } else {
                    dialog.dismiss();
                }



            }
        });



    }

}