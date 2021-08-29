package com.iamyeong.aboutyou;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.iamyeong.aboutyou.dto.Person;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InputFragment extends Fragment {

    private NumberPicker yearPicker, monthPicker, dayPicker;
    private EditText editName, editIntro;
    private CheckBox sexBoxMale, sexBoxFemale;
    private Person person;
    public static final int PERSON_INSERT = 0;
    public static final int PERSON_UPDATE = 1;
    private int optionCode = 0;
    private int sexCode = 0;

    public InputFragment() {
        // Required empty public constructor
    }

    public InputFragment(Person person, int optionCode) {

        this.person = person;
        this.optionCode = optionCode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Date date = new Date();

        View view = inflater.inflate(R.layout.fragment_input, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_input);
        Button finishButton = view.findViewById(R.id.btn_finish_input);
        sexBoxMale = view.findViewById(R.id.chk_box_male_input);
        sexBoxFemale = view.findViewById(R.id.chk_box_female_input);

        yearPicker = view.findViewById(R.id.picker_year_input);
        monthPicker = view.findViewById(R.id.picker_month_input);
        dayPicker = view.findViewById(R.id.picker_day_input);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2021);//getTime
        yearPicker.setValue(1995);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        //년/월에 따라 max 만 조절하면 됨
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(InputFragment.this).commit();
            }
        });

        sexBoxMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sexCode = 1;
                sexBoxFemale.setChecked(false);
                sexBoxMale.setChecked(true);
            }
        });

        sexBoxFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sexCode = 0;
                sexBoxMale.setChecked(false);
                sexBoxFemale.setChecked(true);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Save or Update 코드
                switch(optionCode) {

                    case 0 :
                        //Insert
                        Toast.makeText(getContext(), "Insert!", Toast.LENGTH_SHORT).show();
                        break;

                    case 1 :
                        //Update
                        Toast.makeText(getContext(), "Update!", Toast.LENGTH_SHORT).show();
                        break;

                }

                getActivity().getSupportFragmentManager().beginTransaction().remove(InputFragment.this).commit();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}