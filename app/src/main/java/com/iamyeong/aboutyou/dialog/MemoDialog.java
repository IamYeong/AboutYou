package com.iamyeong.aboutyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.iamyeong.aboutyou.R;

public class MemoDialog extends Dialog {

    private Button confirmButton;

    public MemoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_expand_form);

        confirmButton = findViewById(R.id.btn_dialog);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
