package com.iamyeong.aboutyou;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamyeong.aboutyou.dto.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private List<Person> people;
    private List<Person> copyPeople;

    public PersonViewAdapter() {
        people = new ArrayList<>();
    }

    public void addPerson(Person person) {
        people.add(person);
        notifyDataSetChanged();
    }

    public void filtering(String pattern) {

    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (people == null ? 0 : people.size());
    }
}

class PersonViewHolder extends RecyclerView.ViewHolder {

    public PersonViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
