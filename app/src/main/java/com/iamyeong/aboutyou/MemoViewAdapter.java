package com.iamyeong.aboutyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iamyeong.aboutyou.dialog.MemoDialog;
import com.iamyeong.aboutyou.dto.Memo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoViewAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    private List<Memo> memos;
    private List<Memo> memosForFiltering;
    private Context context;

    public MemoViewAdapter(Context context) {
        memos = new ArrayList<>();
        memosForFiltering = new ArrayList<>();
        this.context = context;
    }

    public void addMemo(Memo memo) {
        memos.add(memo);
        memosForFiltering.add(memo);
        notifyDataSetChanged();
    }

    public void findMemoByTitle(String pattern) {

        memos.clear();

        if (pattern.length() == 0) {
            memos.addAll(memosForFiltering);
        } else {

            for (Memo memo : memosForFiltering) {

                if (memo.getTitle().contains(pattern)) {
                    memos.add(memo);
                }

            }

        }

        notifyDataSetChanged();
    }

    public void sortAscendingOrder() {
        Collections.sort(memos);
        notifyDataSetChanged();
    }

    public void sortDescendingOrder() {
        Collections.sort(memos);
        Collections.reverse(memos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.memo_index, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoDialog dialog = new MemoDialog(context);
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (memos == null ? 0 : memos.size());
    }
}

class MemoViewHolder extends RecyclerView.ViewHolder {

    protected CardView cardView;

    public MemoViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.card_memo);

    }
}
