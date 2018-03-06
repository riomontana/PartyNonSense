package com.lfo.partynonsense;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
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
    private float goalX;
    private float goalY;
    private int currentGoal;
    private int score;
    private ConstraintLayout layout;
    public BallGameSensorListener(ImageView playerIv, ImageView goalIv, ConstraintLayout layout){
        this.playerIv = playerIv;
        this.goalIv = goalIv;
        this.layout = layout;
        xVel = 0;
        yVel = 0;
        score = 0;
        goalX = 200;
        goalY = 200;
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
        if(newXPos > 0 && newXPos < layout.getWidth() - playerIv.getWidth()){
            playerIv.setX(newXPos);
        }else{
            score --;
            xVel = (xVel * -1) / 2;
        }
        if(newYPos > 0 && newYPos < layout.getHeight() - playerIv.getHeight()){
            playerIv.setY(newYPos);
        }else{
            score --;
            yVel = (yVel * -1) / 2;
        }
        float xDiff = newXPos - goalX;
        float yDiff = newYPos - goalY;
        updateGoal();
        if(xDiff > - playerIv.getWidth()+30 && xDiff < goalIv.getWidth()-30 && yDiff < goalIv.getHeight()-30 && yDiff > - playerIv.getHeight()+30){
            score ++;
            newGoal();
            updateGoal();
        }

    }

    public int getScore() {
        return score;
    }
    private int updateGoal(){
        goalIv.setY(goalY);
        goalIv.setX(goalX);
        return currentGoal;
    }
    private void newGoal(){
        Random ran = new Random();
        goalX = ran.nextInt((int) layout.getWidth()-300) + 150;
        goalY = ran.nextInt((int) layout.getHeight() - 300) + 150;
        float xDiff = playerIv.getX() - goalX;
        float yDiff = playerIv.getY() - goalY;
        if(xDiff > - playerIv.getWidth()+30 && xDiff < goalIv.getWidth()-30 && yDiff < goalIv.getHeight()-30 && yDiff > - playerIv.getHeight()+30){
            newGoal();
        }
    }
}
