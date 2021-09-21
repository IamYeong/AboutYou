package com.iamyeong.aboutyou;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    private ProgressBar progressBar;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        setCancelable(false);
        progressBar = findViewById(R.id.progress_bar);
        //customOptions();


    }

}
