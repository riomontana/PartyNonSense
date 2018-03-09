package com.lfo.partynonsense.fragments;


import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
    private static final int ERRORMARGIN = 10;
    private static final int POINTS = 1000;
    private View view;
    private SensorManager sensorManager;
    private Sensor proximitySensor, rotationVector;
    private ImageView ivArrow;
    private TextView tvCurrent, tvComand, tvScore;
    private Random random = new Random();
    private int guessValue;
    private int score = 0;
    private int goalValue;
    private float mCurrentDegree = 0;
    private int direction;
    private boolean gameOn = false;

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
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        }
        ivArrow = (ImageView) view.findViewById(R.id.ivArrow);
        tvCurrent = (TextView) view.findViewById(R.id.tvEnd);
        tvComand = (TextView) view.findViewById(R.id.tvComand);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        tvScore.setText("Score " + 0);
        createGameInfoAlertDialog();
        return view;
    }

    /**
     * Create a new alert dialog
     */
    public void createGameInfoAlertDialog() {
        Log.d(TAG, "createGameInfoAlertDialog: ");
        GameInfoAlertDialogFragment gameInfoDialog = new GameInfoAlertDialogFragment();
        gameInfoDialog.setTitle("Spinner!");
        gameInfoDialog.setText(getResources().getString(R.string.rotate_game_info));
        gameInfoDialog.setImageResource(R.drawable.how_to_volumegame);
        gameInfoDialog.show(getActivity().getFragmentManager(), "Spinner");
    }

    /**
     * Create a new alert dialog when the game stops
     */
    public void createGameStopAlertDialog() {
        GameStopAlertDialog gameStopAlertDialog = new GameStopAlertDialog();
        gameStopAlertDialog.setTitle("Spinner!");
        gameStopAlertDialog.setScore(score);
        gameStopAlertDialog.show(getActivity().getFragmentManager(), "Spinner");
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
            if (sensorEvent.values[0] == 0.0) {
                guessValue = (int) ((mCurrentDegree + 360));
                Log.d(TAG, "guessValue: " + guessValue);
                calculateScore();
                Log.d(TAG, "---------------------------------------------------------------");
                start();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateScore() {
        if (direction == RIGHT) {
            if (guessValue >= (goalValue - ERRORMARGIN) && guessValue <= (goalValue + ERRORMARGIN)) {
                score = score + POINTS;
                tvScore.setText("Score: " + String.valueOf(score));
                Log.d(TAG, "Guessed correct");
            }
        }
        if (direction == LEFT) {
            if (guessValue >= (goalValue - ERRORMARGIN) && guessValue <= (goalValue + ERRORMARGIN)) {
                score = score + POINTS;
                tvScore.setText("Score: " + String.valueOf(score));
                Log.d(TAG, "Guessed correct");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        gameOn = true;
        String moveTo;
        int startValue = (int) (((mCurrentDegree) * -1) - 360) * -1;
        Log.d(TAG, "start: " + startValue);
        int leftRight = random.nextInt(2);
        double randomNumber = random.nextInt(170) + 10;
        int moveDegrees = (int) Math.round(randomNumber / 10.0) * 10;
        Log.d(TAG, "move " + moveDegrees + " degrees");
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
        Log.d(TAG, "goalValue: " + goalValue);
    }

    @Override
    public void stop() {
        gameOn = false;
        createGameStopAlertDialog();

    }

    @Override
    public int getScore() {
        return score;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (gameOn) {
            rotateArrow(sensorEvent);
            lockGuess(sensorEvent);
        }
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
