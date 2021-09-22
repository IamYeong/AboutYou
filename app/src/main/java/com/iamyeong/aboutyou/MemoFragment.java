package com.iamyeong.aboutyou;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Memo;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;
import com.iamyeong.aboutyou.listener.OnFragmentDataNotifyListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MemoFragment extends Fragment {

    private EditText title, content;
    private Context context;
    private OnFragmentDataNotifyListener<Memo> listener;
    private Memo memo;


    public MemoFragment() {
        // Required empty public constructor
    }

    public void setDataListener(OnFragmentDataNotifyListener<Memo> listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        Button confirmButton;

        title = view.findViewById(R.id.et_title_memo_fragment);
        content = view.findViewById(R.id.et_content_memo_fragment);
        confirmButton = view.findViewById(R.id.btn_memo_fragment_confirm);

        memo = (Memo) getArguments().getSerializable("MEMO");
        title.setText(memo.getTitle());
        content.setText(memo.getContent());
        memo.setDocumentId(null);

        Toolbar toolbar = view.findViewById(R.id.toolbar_memo);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwoButtonDialog dialog = new TwoButtonDialog(context, TwoButtonDialog.DIALOG_FINISH_MEMO);
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(boolean selectButton) {

                        dialog.dismiss();
                        if (selectButton) {
                            removeFragment();
                        }

                    }
                });
                dialog.show();

            }
        });

        ImageView deleteImage = view.findViewById(R.id.img_delete_memo);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwoButtonDialog dialog = new TwoButtonDialog(context, TwoButtonDialog.DIALOG_DELETE_MEMO);
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(boolean selectButton) {

                        if (selectButton) {
                            deleteMemo();
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

                updateMemo();
                listener.onFragmentListener(memo);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateMemo() {

        String titleChar = title.getText().toString();
        String contentChar = content.getText().toString();
        //날짜

        memo.setTitle(titleChar);
        memo.setContent(contentChar);



    }

    private void deleteMemo() {

    }

    private void removeFragment() {

    }

}