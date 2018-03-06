package com.lfo.partynonsense;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class BallGameFragment extends Fragment implements FragmentTemplate {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private SensorManager sensorManager;
    private Sensor sensor;
    private BallGameSensorListener sensorListener;
    private ConstraintLayout ballLayout;
    private ImageView goalIv;
    private ImageView playerIv;
    private Integer score;
    public BallGameFragment() {
        // Required empty public constructor
    }

    public static BallGameFragment newInstance(String param1, String param2) {
        BallGameFragment fragment = new BallGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ball_game, container, false);
        playerIv = (ImageView) v.findViewById(R.id.imageView);
        goalIv = (ImageView) v.findViewById(R.id.imageView2);
        ballLayout = v.findViewById(R.id.ballLayout);
        sensorManager = (SensorManager)getActivity().getSystemService
                (Context.SENSOR_SERVICE );
        if(sensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER)!= null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        sensorListener = new BallGameSensorListener(playerIv, goalIv, ballLayout);
        score = 0;
        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void onResume() {
        super.onResume();
        start();
    }
    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    public void onDestroy() {
        super.onDestroy();
        sensorManager = null;
        sensor = null;
        sensorListener = null;
    }

    public void start() {
        sensorManager.registerListener(sensorListener, sensor,
                SensorManager.SENSOR_DELAY_GAME);
    }
    public void stop() {
        score = sensorListener.getScore();
        sensorManager.unregisterListener(sensorListener);
    }
    public int getScore() {
        score = sensorListener.getScore();
        return score;
    }
    public void reset() {

    }
}
