package com.iamyeong.aboutyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iamyeong.aboutyou.R;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;

public class TwoButtonDialog extends Dialog {

    private int optionCode = 0;

    public static final int DIALOG_APP_EXIT = 0;
    public static final int DIALOG_ACTIVITY_EXIT = 1;
    public static final int DIALOG_EMAIL_ACCOUNT = 2;
    public static final int DIALOG_DELETE_MEMO = 3;
    public static final int DIALOG_FINISH_MEMO = 4;


    private OnDialogButtonClickListener listener;

    public TwoButtonDialog(@NonNull Context context, int optionCode) {
        super(context);
        this.optionCode = optionCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_two_button);

        Button confirmButton, cancelButton;
        confirmButton = findViewById(R.id.btn_confirm_dialog);
        cancelButton = findViewById(R.id.btn_cancel_dialog);

        TextView title, content;
        title = findViewById(R.id.tv_title_dialog);
        content = findViewById(R.id.tv_content_dialog);

        switch (optionCode) {

            case 0 :
                title.setText(R.string.dialog_app_exit_title);
                break;

            case 1 :
                title.setText(R.string.dialog_activity_exit_title);
                break;

            case 2 :
                title.setText(R.string.dialog_create_email_account_title);
                content.setText(R.string.dialog_create_email_account_content);
                break;

            case 3 :
                title.setText(R.string.dialog_delete_memo_title);
                content.setText(R.string.dialog_delete_memo_content);
                break;

            case 4 :
                title.setText(R.string.dialog_finish_memo_title);
                content.setText(R.string.dialog_finish_memo_content);
                break;

        }


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onDialogButtonClick(true);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onDialogButtonClick(false);
                dismiss();
            }
        });

    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        this.listener = listener;
    }

}
