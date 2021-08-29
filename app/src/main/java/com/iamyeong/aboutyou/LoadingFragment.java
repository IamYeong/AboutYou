package com.iamyeong.aboutyou;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.squareup.okhttp.internal.RouteDatabase;


public class LoadingFragment extends Fragment {


    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        /*
        RotateAnimation animation = new RotateAnimation(

        );
        animation.setDuration(500);

        ImageView imageView = view.findViewById(R.id.);
        imageView.startAnimation(animation);

         */

        return view;
    }



}