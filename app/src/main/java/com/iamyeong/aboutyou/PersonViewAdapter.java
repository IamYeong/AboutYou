package com.iamyeong.aboutyou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.auth.User;
import com.iamyeong.aboutyou.dto.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private List<Person> people;
    private List<Person> copyPeople;
    private Context context;
    private FirebaseUser user;

    public PersonViewAdapter(Context context) {
        people = new ArrayList<>();
        copyPeople = new ArrayList<>();
        this.context = context;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public void addPerson(Person person) {
        people.add(person);
        copyPeople.add(person);
        notifyDataSetChanged();
    }

    public void clearPeople() {

        if (people != null) {
            people.clear();
            copyPeople.clear();
        }

    }

    public void filtering(String pattern) {

        people.clear();

        if (pattern.length() == 0) {
            people.addAll(copyPeople);
        } else {

            for (Person person : copyPeople) {

                if (person.getName().contains(pattern)) {
                    people.add(person);
                }
            }

        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_index_form, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {

        Person person = people.get(position);

        holder.name.setText(person.getName());
        //...

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformationActivity.class);
                intent.putExtra("PERSON", person);
                intent.putExtra("USER", user);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (people == null ? 0 : people.size());
    }
}

class PersonViewHolder extends RecyclerView.ViewHolder {

    protected TextView name, description;
    protected ImageView profileImage, settingImage;
    protected Button button1, button2;
    protected CardView cardView;

    public PersonViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_profile_name);
        description = itemView.findViewById(R.id.tv_profile_description);
        profileImage = itemView.findViewById(R.id.img_profile);
        settingImage = itemView.findViewById(R.id.img_setting);
        button1 = itemView.findViewById(R.id.btn_profile);
        button2 = itemView.findViewById(R.id.btn_edit);
        cardView = itemView.findViewById(R.id.card_person);
    }

}
