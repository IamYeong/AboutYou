package com.iamyeong.aboutyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.iamyeong.aboutyou.dialog.MemoDialog;
import com.iamyeong.aboutyou.dto.Memo;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnFragmentDataNotifyListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MemoViewAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    private List<Memo> memos;
    private List<Memo> memosForFiltering;
    private Context context;
    private Person person;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public MemoViewAdapter(Context context) {
        memos = new ArrayList<>();
        memosForFiltering = new ArrayList<>();
        this.context = context;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void addMemo(Memo memo) {
        memos.add(memo);
        memosForFiltering.add(memo);
        notifyDataSetChanged();
    }

    public void clearMemos() {

        if (memos != null) {
            memos.clear();
            memosForFiltering.clear();

        }

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
        String contentPart = "";
        if (memo.getContent().length() > 20) {
            contentPart = memo.getContent().substring(0, 20) + "...";
        } else {
            contentPart = memo.getContent();
        }

        String date = dateFormat.format(memo.getDate());

        holder.title.setText(memo.getTitle());
        holder.content.setText(contentPart);
        holder.date.setText(date);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MemoActivity.class);

                intent.putExtra("MEMO", memo);
                intent.putExtra("PERSON", person);
                context.startActivity(intent);

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
    TextView title, content, date;

    public MemoViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.card_memo);
        title = itemView.findViewById(R.id.tv_title_memo_list);
        content = itemView.findViewById(R.id.tv_content_memo_list);
        date = itemView.findViewById(R.id.tv_date_memo_list);

    }
}
