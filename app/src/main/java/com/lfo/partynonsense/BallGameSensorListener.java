package com.lfo.partynonsense;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Alexander on 2018-02-22.
 */

public class BallGameSensorListener implements SensorEventListener {
    private ImageView playerIv;
    private ImageView goalIv;
    private float xVel;
    private float yVel;
    private int currentGoal;
    private float[][] list;
    private int score;

    public BallGameSensorListener(ImageView playerIv, ImageView goalIv){
        this.playerIv = playerIv;
        this.goalIv = goalIv;
        xVel = 0;
        yVel = 0;
        score = 0;
        list = new float[4][2];
        list[0][0] = 100;
        list[0][1] = 100;
        list[1][0] = 100;
        list[1][1] = 800;
        list[2][0] = 1400;
        list[2][1] = 100;
        list[3][0] = 1400;
        list[3][1] = 800;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        xVel = xVel + event.values[0];
        yVel = yVel + event.values[1];
        moveBall(xVel, yVel);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void moveBall(float x, float y){
        float newXPos = playerIv.getX() - x/2;
        float newYPos = playerIv.getY () + y/2;
        if(newXPos > 0 && newXPos < 900){
            playerIv.setX(newXPos);
        }else{
            xVel = 0;
        }
        if(newYPos > 0 && newYPos < 1500){
            playerIv.setY(newYPos);
        }else{
            yVel = 0;
        }
        float xDiff = newXPos - list[currentGoal][1];
        float yDiff = newYPos - list[currentGoal][0];
        updateGoal();
        if(xDiff > -150 && xDiff < 50 && yDiff < 50 && yDiff > -150){
            score ++;
            Random ran = new Random();
            currentGoal = ran.nextInt(4);
            updateGoal();
        }

    }

    public int getScore() {
        return score;
    }
    private int updateGoal(){
        goalIv.setY(list[currentGoal][0]);
        goalIv.setX(list[currentGoal][1]);
        return currentGoal;
    }
}
