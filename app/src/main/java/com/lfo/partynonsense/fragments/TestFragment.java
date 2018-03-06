package com.lfo.partynonsense.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lfo.partynonsense.activitys.GameActivity;
import com.lfo.partynonsense.R;


public class TestFragment extends Fragment {

    private GameActivity gameActivity;
    private TextView textView;
    private View view;
    private String blablabla;

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(blablabla);
        Button button = (Button) view.findViewById(R.id.btnChange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeGame();

            }
        });

        return view;
    }

    public void setText(String game) {
        this.blablabla=game;
//        textView.setText("Detta är spel : " + game);
    }
}
