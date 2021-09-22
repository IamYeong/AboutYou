package com.iamyeong.aboutyou;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    //SimpleDataFormat

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

    public void clearMemos() {

        memos.clear();
        memosForFiltering.clear();

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

        Memo memo = memos.get(position);

        holder.title.setText(memo.getTitle());
        holder.content.setText(memo.getContent());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (memos == null ? 0 : memos.size());
    }
}

class MemoViewHolder extends RecyclerView.ViewHolder {

    //Default
    CardView cardView;
    TextView title, content;

    public MemoViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.card_memo);
        title = itemView.findViewById(R.id.tv_title_memo_list);
        content = itemView.findViewById(R.id.tv_content_memo_list);

    }
}
