package com.iamyeong.aboutyou;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamyeong.aboutyou.dto.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoViewAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    private List<Memo> memos;

    public MemoViewAdapter() {
        memos = new ArrayList<>();
    }

    public void addMemo(Memo memo) {
        memos.add(memo);
        notifyDataSetChanged();
    }

    public void memoFiltering(String pattern) {



    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class MemoViewHolder extends RecyclerView.ViewHolder {
    public MemoViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
