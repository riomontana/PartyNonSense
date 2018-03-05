package com.lfo.partynonsense;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Vibrator;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment for decibel matching game
 *
 * @author Linus Forsberg
 */
public class AmplitudeGameFragment extends Fragment {

    private ProgressBar ampProgressBar;
    private ProgressBar countProgressBar;
    private TextView tvAmpValue;
    private TextView tvCount;
    private MediaRecorder mediaRecorder = null;
    private boolean gameRunning;
    private int amplitude;
    private Random random = new Random();
    private int maxAmpValue = 32768;
    private int goalValue = random.nextInt(maxAmpValue - 5000) + 5000;
    private Vibrator vibrator;
    private Timer timer = new Timer();
    private int count;
    private boolean inInterval = false;
    private int playerScore = 0;
    private TextView tvScore; // todo ta bort denna sen

    /**
     * Required empty constructor
     */
    public AmplitudeGameFragment() {

    }


    /**
     * Create necessary GUI components
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dbmeter, container, false);
        tvAmpValue = view.findViewById(R.id.tvProgressBar1Title);
        tvCount = view.findViewById(R.id.tvProgressBar2Title);
        tvScore = view.findViewById(R.id.tvScore); // todo ta bort denna sen
        ampProgressBar = view.findViewById(R.id.ampProgressBar);
        ampProgressBar.setMax(maxAmpValue);
        countProgressBar = view.findViewById(R.id.countProgressBar);
        countProgressBar.setMax(100);
        return view;
    }

    /**
     * Start recording audio, set game to running and start checking the amplitude
     */
    @Override
    public void onResume() {
        super.onResume();
        startMediaRecorder();
        gameRunning = true;
        startAmplitudeCheck();
    }

    /**
     * Set game to not running which stops the thread from checking amplitude values and stop audio recorder
     */
    @Override
    public void onPause() {
        super.onPause();
        gameRunning = false;
        stopMediaRecorder();
    }

    /**
     * Start new thread checking every 100ms for amplitude values.
     * Converting amp values to dB value and displays in textview and progressbar
     */
    private void startAmplitudeCheck() {
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        ampProgressBar.setSecondaryProgress(goalValue);
        Log.d("Goal value: ", String.valueOf(goalValue));
        new Thread() {
            public void run() {
                while (gameRunning) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                amplitude = (int) getAmplitude();
                                ampProgressBar.setProgress(amplitude);
//                                tvAmpValue.setText(String.valueOf(amplitude));

                                if (amplitude < (goalValue + 2000) && amplitude > (goalValue - 2000)) {
                                    inInterval = true;
                                    counter();
                                } else {
                                    inInterval = false;
                                }
                            }
                        });
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Fills a progressbar if user amplitude matches the goal amplitude + - 2000
     */
    private void counter() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if (inInterval) {
//                            tvCount.setText("Count: " + count);
//                            ampProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                            countProgressBar.setProgress(count);
                            count++;
//                        } else {
//                            ampProgressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                        }
                        if (count == 100) {
                            vibrator.vibrate(250);
                            goalValue = random.nextInt(maxAmpValue - 5000) + 5000;
                            ampProgressBar.setSecondaryProgress(goalValue);
                            countProgressBar.setProgress(count);
                            playerScore += 1000;
                            tvScore.setText("Score: " + playerScore);// todo ta bort denna sen
                            count = 0;
                        }
                    }
                });
            }
        }, 1000, 1000);
    }


    /**
     * Start recording audio
     */
    public void startMediaRecorder() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile("/dev/null");
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
        }
    }

    /**
     * Stop recording audio
     */
    public void stopMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    /**
     * Get maximum amplitude and returns the value to calling method
     *
     * @return maximum amplitude value
     */
    public double getAmplitude() {
        if (mediaRecorder != null)
            return mediaRecorder.getMaxAmplitude();
        else
            return 0;

    }
}
