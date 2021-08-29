package com.iamyeong.aboutyou;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.iamyeong.aboutyou.dto.Person;

public class InputFragment extends Fragment {

    private NumberPicker yearPicker, monthPicker, dayPicker;
    private EditText editName, editIntro;
    private Person person;
    public static final int PERSON_INSERT = 0;
    public static final int PERSON_UPDATE = 1;
    private int optionCode = 0;


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
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_input);
        Button finishButton = view.findViewById(R.id.btn_finish_input);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(InputFragment.this).commit();
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