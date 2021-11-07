package com.iamyeong.aboutyou;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    private TextView errorMessage;
    private Button errorConfirmButton;
    private Intent lastActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Intent intent = getIntent();

        errorMessage = findViewById(R.id.tv_error_message);
        errorConfirmButton = findViewById(R.id.btn_error_confirm);

        errorConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(lastActivityIntent);
                finish();
            }
        });

        errorMessage.setText(intent.getStringExtra("ERROR_MESSAGE"));
        lastActivityIntent = intent.getParcelableExtra("LAST_ACTIVITY_INTENT");

    }
}