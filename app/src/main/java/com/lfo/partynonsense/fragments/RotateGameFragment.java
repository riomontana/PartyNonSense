package com.lfo.partynonsense.fragments;


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

    private View view;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor, proximitySensor;
    private ImageView ivArrow, ivCircle;
    private TextView tvStart, tvGoal, tvCurrent, tvGuess, tvComand, tvScore;
    private Button btnReset;
    private Random random = new Random();
    private int startValue, guessValue, score = -1, goalValue;
    private float mCurrentDegree = 0;
    private long lastUpdateTime = System.currentTimeMillis();
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mRotationMatrix = new float[9];
    private float[] mOrientation = new float[3];


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
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        ivArrow = (ImageView) view.findViewById(R.id.ivArrow);
        ivCircle = (ImageView) view.findViewById(R.id.ivCircle);
        tvStart = (TextView) view.findViewById(R.id.tvStart);
        tvCurrent = (TextView) view.findViewById(R.id.tvEnd);
        tvGuess = (TextView) view.findViewById(R.id.tvGuess);
        tvGoal = (TextView) view.findViewById(R.id.tvGoal);
        tvComand = (TextView) view.findViewById(R.id.tvComand);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        return view;
    }

    public void rotateUsingOrientationAPI(SensorEvent event) {
        if (event.sensor == accelerometerSensor) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == magnetometerSensor) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        //only 4 times in 1 second
        if (mLastAccelerometerSet && mLastMagnetometerSet && System.currentTimeMillis() - lastUpdateTime > 250) {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
//            tvStart.setText(String.valueOf((int) mCurrentDegree)); //TODO Remove
            tvCurrent.setText("current " + String.valueOf((int) azimuthInDegress));
            RotateAnimation mRotateAnimation = new RotateAnimation(
                    mCurrentDegree, azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

//            if (((mCurrentDegree > 270 && mCurrentDegree < 360) && (azimuthInDegress >= 0 && azimuthInDegress < 90) ||
//                    ((mCurrentDegree < 90 && mCurrentDegree >= 0)) && (azimuthInDegress > 270 && azimuthInDegress < 360))) {
//                mRotateAnimation.setDuration(0);
//            } else {
            mRotateAnimation.setDuration(250);
//            }

            mRotateAnimation.setFillAfter(true);
            ivArrow.startAnimation(mRotateAnimation);
            mCurrentDegree = azimuthInDegress;
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    private void lockGuess(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == proximitySensor) {
            guessValue = (int) mCurrentDegree;
            tvGuess.setText("guess " + String.valueOf(guessValue));
            calculateScore();
        }
    }

    private void calculateScore() {
        Log.d(TAG, "startValue: " + startValue);
        Log.d(TAG, "guessValue: " + guessValue);
        Log.d(TAG, "goalValue " + goalValue);
        if (startValue + guessValue > 360) {
            guessValue = guessValue - 360;
            Log.d(TAG, "if( > 360 ) guessValue changed to " + guessValue);
        }
        Log.d(TAG, "calculateScore: start+guess=" + (startValue + guessValue) + " >= goalvalue=" + goalValue);
        if ((startValue + guessValue >= (startValue + goalValue) - 20) && (startValue + guessValue <= (startValue + goalValue) + 20)) {
            score++;
            tvScore.setText("Score: " + String.valueOf(score));
        }
    }

    @Override
    public void start() {
        int leftRight = random.nextInt(2);
//        goalValue = random.nextInt(359) + 1;
        goalValue = 90;
        String direction = "";
        if (leftRight == 0) {
            direction = "Right";
        } else {
            direction = "Left";
        }
        tvComand.setText("Move " + goalValue + " degrees to the " + direction);
        startValue = (int) mCurrentDegree;

        tvStart.setText("Start " + String.valueOf((int) mCurrentDegree));
        if ((startValue + goalValue) > 360) {
            tvGoal.setText("Goal " + String.valueOf((goalValue + startValue - 360)));
        } else {
            tvGoal.setText("Goal " + String.valueOf((goalValue + startValue)));
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public int getScore() {
        return score;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        rotateUsingOrientationAPI(sensorEvent);
        lockGuess(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
        sensorManager.unregisterListener(this, proximitySensor);
    }

}
