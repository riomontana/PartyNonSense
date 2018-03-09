package com.lfo.partynonsense.fragments;


import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfo.partynonsense.FragmentTemplate;
import com.lfo.partynonsense.R;

import java.util.Random;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RotateGameFragment extends Fragment implements FragmentTemplate, SensorEventListener {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private View view;
    private SensorManager sensorManager;
    private Sensor proximitySensor, rotationVector;
    private ImageView ivArrow;
    private TextView tvStart, tvGoal, tvCurrent, tvGuess, tvComand, tvScore;
    private Button btnReset;
    private Random random = new Random();
    private int startValue, guessValue, score = 0, goalValue;
    private float mCurrentDegree = 0;
    //    private float startDegree = 0;
    private int direction;

    private static final String TAG = "LOG";

    public RotateGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rotate_game, container, false);
        sensorManager = (SensorManager) getActivity().getSystemService
                (SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        ivArrow = (ImageView) view.findViewById(R.id.ivArrow);
        tvStart = (TextView) view.findViewById(R.id.tvStart);
        tvCurrent = (TextView) view.findViewById(R.id.tvEnd);
        tvGuess = (TextView) view.findViewById(R.id.tvGuess);
        tvGoal = (TextView) view.findViewById(R.id.tvGoal);
        tvComand = (TextView) view.findViewById(R.id.tvComand);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        tvScore.setText("Score " + 0);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        start();
        return view;
    }

    public void rotateArrow(SensorEvent event) {
        //Game Rotation Sensor
        if (event.sensor == rotationVector) {
            float eventValue = event.values[2];
            float angleInDegress = (((eventValue + 1) * 180));
            tvCurrent.setText(String.valueOf((int) (angleInDegress - 360) * -1));
            RotateAnimation mRotateAnimation = new RotateAnimation(
                    mCurrentDegree, -angleInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            //250 milliseconds
            mRotateAnimation.setDuration(250);
            mRotateAnimation.setFillAfter(true);
            ivArrow.startAnimation(mRotateAnimation);
            mCurrentDegree = -angleInDegress;

        }
    }

    private void lockGuess(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == proximitySensor) {
            guessValue = (int) ((mCurrentDegree + 360));
            tvGuess.setText("guess " + String.valueOf(guessValue));
            calculateScore();
            start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateScore() {
        int playerGuess;
        if (direction == RIGHT) {
            playerGuess = startValue + guessValue;
            if (playerGuess > 359) playerGuess = playerGuess - 360;
            if (playerGuess >= (mCurrentDegree - 20) && playerGuess <= (mCurrentDegree + 20)) {
                score++;
                tvScore.setText("Score: " + String.valueOf(score));
            }
        }
        if (direction == LEFT) {
            playerGuess = startValue - guessValue;
            if (playerGuess < 0) playerGuess = playerGuess + 359;
            if (playerGuess >= (playerGuess - 20) && playerGuess <= (playerGuess + 20)) {
                score++;
                tvScore.setText("Score: " + String.valueOf(score));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        String moveTo;
//        startDegree = 0;
        startValue = (int) (((mCurrentDegree) * -1) - 360) * -1;
        int leftRight = random.nextInt(2);
//        moveDegrees = random.nextInt(179) + 1;
        int moveDegrees = 90;

        if (leftRight == RIGHT) {
            moveTo = "Right";
            direction = RIGHT;
            goalValue = startValue + moveDegrees;
            if (goalValue > 359) goalValue = goalValue - 360;
        } else {
            moveTo = "Left";
            direction = LEFT;
            goalValue = startValue - moveDegrees;
            if (goalValue < 0) goalValue = goalValue + 360;
        }
        tvComand.setText("Move " + moveDegrees + " degrees to the " + moveTo);
        tvStart.setText("Start: " + String.valueOf(startValue));
        tvGoal.setText("Goal: " + String.valueOf(goalValue));
    }

    @Override
    public void stop() {

    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void reset() {

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (startDegree == 0) {
//            if (sensorEvent.sensor == rotationVector) {
//                startDegree = ((sensorEvent.values[2] + 1) * 180);
//            }
//        }
        rotateArrow(sensorEvent);
        lockGuess(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, proximitySensor);
        sensorManager.unregisterListener(this, rotationVector);
    }

}
