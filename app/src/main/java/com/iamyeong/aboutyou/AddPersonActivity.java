package com.iamyeong.aboutyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamyeong.aboutyou.dialog.TwoButtonDialog;
import com.iamyeong.aboutyou.dto.Person;
import com.iamyeong.aboutyou.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//추가하거나 수정할 때 사용
public class AddPersonActivity extends AppCompatActivity {

    private Person person;
    private Toolbar toolbar;
    private EditText nameEdit, groupEdit, descEdit;
    private NumberPicker yearPicker, monthPicker, dayPicker;
    private Spinner groupSpinner;
    private FirebaseFirestore db;
    private CollectionReference collection;
    private Button confirmButton;
    private List<String> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        //View
        allFindViewById();
        pickerInit();
        spinnerInit();

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("PERSON");

        db = FirebaseFirestore.getInstance();
        collection = db.collection(getPackageName())
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("PEOPLE");


        //Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoButtonDialog dialog = new TwoButtonDialog(AddPersonActivity.this, TwoButtonDialog.DIALOG_ACTIVITY_EXIT);
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(boolean selectButton) {

                        if (selectButton) {
                            dialog.dismiss();
                            finish();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String group = groups.get(position);
                groupEdit.setText(group);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (person.getPathId().equals("")) {

                    person.setName(nameEdit.getText().toString());
                    person.setBirthYear(yearPicker.getValue());
                    person.setBirthMonth(monthPicker.getValue());
                    person.setBirthDay(dayPicker.getValue());
                    person.setStarted(false);
                    person.setTinyInfo(descEdit.getText().toString());
                    person.setGroup(groupEdit.getText().toString());

                    addPerson(person);

                } else {

                    person.setName(nameEdit.getText().toString());
                    person.setBirthYear(yearPicker.getValue());
                    person.setBirthMonth(monthPicker.getValue());
                    person.setBirthDay(dayPicker.getValue());
                    person.setStarted(false);
                    person.setTinyInfo(descEdit.getText().toString());
                    person.setGroup(groupEdit.getText().toString());

                    updatePerson(person);

                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        displayInfo();

    }

    private void addPerson(Person p) {

        Map<String, Object> map = new HashMap<>();

        map.put("person_name", p.getName());
        map.put("person_group", p.getGroup());
        map.put("person_birth_year", p.getBirthYear());
        map.put("person_birth_month", p.getBirthMonth());
        map.put("person_birth_day", p.getBirthDay());
        //...

        collection.add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {


                        finish();

                    }
                });


    }

    private void updatePerson(Person p) {

        Map<String, Object> map = new HashMap<>();

        map.put("person_name", p.getName());
        map.put("person_group", p.getGroup());
        map.put("person_birth_year", p.getBirthYear());
        map.put("person_birth_month", p.getBirthMonth());
        map.put("person_birth_day", p.getBirthDay());
        //...

        collection.document(p.getPathId())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        finish();
                    }
                });


    }

    private void selectGroups() {

        groups = new ArrayList<>();




    }

    private void displayInfo() {

        nameEdit.setText(person.getName());
        descEdit.setText(person.getTinyInfo());
        yearPicker.setValue(person.getBirthYear());
        monthPicker.setValue(person.getBirthMonth());
        dayPicker.setValue(person.getBirthDay());
        groupEdit.setText(person.getGroup());

    }

    private void pickerInit() {

        Calendar calendar = Calendar.getInstance();

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        yearPicker.setValue(calendar.get(Calendar.YEAR));

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        yearPicker.setValue(calendar.get(Calendar.MONTH));

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));

    }

    private void spinnerInit() {

        selectGroups();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                AddPersonActivity.this, R.layout.support_simple_spinner_dropdown_item, groups);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        groupSpinner.setAdapter(arrayAdapter);

    }

    private void allFindViewById() {

        toolbar = findViewById(R.id.toolbar_add);
        nameEdit = findViewById(R.id.et_name_add);
        groupEdit = findViewById(R.id.et_club_add);
        descEdit = findViewById(R.id.et_intro_add);
        groupSpinner = findViewById(R.id.spinner_club_add);
        yearPicker = findViewById(R.id.picker_year_add);
        monthPicker = findViewById(R.id.picker_month_add);
        dayPicker = findViewById(R.id.picker_day_add);
        confirmButton = findViewById(R.id.btn_finish_add);


    }

}