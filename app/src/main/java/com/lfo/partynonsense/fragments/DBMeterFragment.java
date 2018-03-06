package com.lfo.partynonsense.fragments;


import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lfo.partynonsense.R;

import java.io.IOException;

/**
 * Fragment for decibel matching game
 * @author Linus Forsberg
 */
public class DBMeterFragment extends Fragment {

    private ProgressBar dbProgressBar;
    private TextView tvDbValue;
    private MediaRecorder mRecorder = null;
    private boolean gameRunning;
    private int decibelValue;
    private int amplitude;

    /**
     * Required empty constructor
     */
    public DBMeterFragment() {

    }


    /**
     * Create necessary GUI components
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dbmeter, container, false);
        tvDbValue = view.findViewById(R.id.tvDbValue);
        dbProgressBar = view.findViewById(R.id.dbProgressBar);
        dbProgressBar.setMax(100);
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
        new Thread() {
            public void run() {
                while (gameRunning) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                amplitude = (int) getAmplitude();
//                                decibelValue = (int) (20 * Math.log10(Math.abs(amplitude) / 32768));
                                decibelValue = (int) (20 * Math.log10(Math.abs(amplitude)));
                                dbProgressBar.setProgress(decibelValue);
                                tvDbValue.setText(String.valueOf(decibelValue));
                                Log.d("Sound pressure", String.valueOf(decibelValue));
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
     * Start recording audio
     */
    public void startMediaRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    /**
     * Stop recording audio
     */
    public void stopMediaRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * Get maximum amplitude and returns the value to calling method
     * @return maximum amplitude value
     */
    public double getAmplitude() {
        if (mRecorder != null)
            return mRecorder.getMaxAmplitude();
        else
            return 0;

    }
}

