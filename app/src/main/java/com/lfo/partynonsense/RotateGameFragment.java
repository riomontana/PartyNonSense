package com.lfo.partynonsense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class RotateGameFragment extends Fragment {

    private GameActivity gameActivity;
    private TextView textView;
    private View view;

    public RotateGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rotate_game, container, false);
        textView = (TextView) view.findViewById(R.id.textView2);
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

//        textView.setText("Detta är spel : " + game);
    }
}
