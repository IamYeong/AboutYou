package com.iamyeong.aboutyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.iamyeong.aboutyou.R;

public class MemoDialog extends Dialog {

    public MemoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_expand_form);


    }
}
