package com.iamyeong.aboutyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iamyeong.aboutyou.dto.Memo;

public class InformationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemoViewAdapter memoViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        recyclerView = findViewById(R.id.rv_info);
        memoViewAdapter = new MemoViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memoViewAdapter);

        memoViewAdapter.addMemo(new Memo());
    }
}