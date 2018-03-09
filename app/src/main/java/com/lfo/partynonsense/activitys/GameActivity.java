package com.lfo.partynonsense.activitys;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.lfo.partynonsense.AmplitudeGameFragment;
import com.lfo.partynonsense.GameInfoAlertDialogFragment;
import com.lfo.partynonsense.R;
import com.lfo.partynonsense.fragments.RotateGameFragment;
import com.lfo.partynonsense.fragments.TestFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 *
 * @author Jimmy Åkesson
 *
 * edited by Linus Forsberg
 * Added count down timer in StartCountDown method
 * Added functionality for Alert Dialog Fragment in changeGame method
 */

public class GameActivity extends AppCompatActivity {

    public final static int GAMES_TO_PLAY = 3;
    public final static int NUMBER_OF_GAMES = 4;
    public final static int ROTATE_GAME = 1;
    public final static int WHACK_A_MOLE_GAME = 2;
    public final static int CONNECTING_DOTS_GAME = 3;
    public final static int SOUND_SENSOR_GAME = 4;

    private TextView tvPlayer, clockTimer, tvScore;
    private HashMap playerScore = new LinkedHashMap<String, Integer>();
    private int nbrOfPlayers;
    private int currentPlayer = 0;
    private int currentGame = 0;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ArrayList<Integer> gameFragmentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //TODO Testvalues REMOVE
        playerScore.put("Player 1", 10);
        playerScore.put("Player 2", 20);
        playerScore.put("Player 3", 15);
        //TODO Testvalues REMOVE

        Intent intent = getIntent();
        nbrOfPlayers = (Integer) intent.getIntExtra("nbrOfPlayers", -1);

        initTopbar();
        randomGames();

        nextPlayer();


    }

    private void initTopbar() {
        tvPlayer = (TextView) findViewById(R.id.tvPlayer);
        clockTimer = (TextView) findViewById(R.id.clockTimer);
        tvScore = (TextView) findViewById(R.id.tvScore);
    }

    private void setTopbarValues(int player) {
        tvPlayer.setText(playerScore.keySet().toArray()[player - 1].toString());
        tvScore.setText("Score: " + playerScore.get(playerScore.keySet().toArray()[player - 1].toString()).toString());
    }

    private void randomGames() {
        Random rand = new Random();
        int game;

        for (int x = 0; x < 3; x++) {
            boolean run = true;
            while (run) {
                game = rand.nextInt(NUMBER_OF_GAMES) + 1;
                if (!gameFragmentsList.contains(game)) {
                    gameFragmentsList.add(game);
                    run = false;
                }
            }
        }
        Log.d("LOG", "randomGames: " + Arrays.toString(gameFragmentsList.toArray()));
    }

    private void nextPlayer() {
        if (currentPlayer < nbrOfPlayers) {
            setTopbarValues(++currentPlayer);
            currentGame = 0;
            changeGame();
        } else {
            this.finish();
            //TODO GAME OVER
        }
    }

    public void changeGame() {
        clockTimer.setText("Get ready!");
        Bundle bundle = new Bundle();
        String gameName;

        if (currentGame < GAMES_TO_PLAY) {
            currentGame++;
            switch (gameFragmentsList.get(currentGame - 1)) {
                case ROTATE_GAME:
                    gameName = "ROTATE GAME";
                    bundle.putString("gameName", gameName);
                    RotateGameFragment fragment = new RotateGameFragment();
                    setFragment(fragment,"ROTATE_GAME");
                    DialogFragment dialogFragment = new GameInfoAlertDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), gameName);
                    break;
                case WHACK_A_MOLE_GAME:
                    gameName = "WHACK A MOLE";
                    bundle.putString("gameName", gameName);
                    TestFragment fragment2 = new TestFragment();
                    setFragment(fragment2, "WHACK_A_MOLE_GAME");
                    fragment2.setText(gameName);
                    DialogFragment dialogFragment2 = new GameInfoAlertDialogFragment();
                    dialogFragment2.setArguments(bundle);
                    dialogFragment2.show(getFragmentManager(), gameName);
                    break;
                case CONNECTING_DOTS_GAME:
                    gameName = "CONNECTING DOTS";
                    bundle.putString("gameName", gameName);
                    TestFragment fragment3 = new TestFragment();
                    setFragment(fragment3, "CONNECTING_DOTS_GAME");
                    fragment3.setText(gameName);
                    DialogFragment dialogFragment3 = new GameInfoAlertDialogFragment();
                    dialogFragment3.setArguments(bundle);
                    dialogFragment3.show(getFragmentManager(), gameName);
                    break;
                case SOUND_SENSOR_GAME:
                    gameName = "SOUND GAME";
                    bundle.putString("gameName", gameName);
                    AmplitudeGameFragment fragment4 = new AmplitudeGameFragment();
                    setFragment(fragment4, "SOUND_SENSOR_GAME");
//                    fragment4.setText(gameName);
                    DialogFragment dialogFragment4 = new GameInfoAlertDialogFragment();
                    dialogFragment4.setArguments(bundle);
                    dialogFragment4.show(getFragmentManager(), gameName);
                    break;
            }
        } else {
            nextPlayer();
        }
    }

    /**
     * Start count down timer and update user interface
     */
    public void startCountDownTimer() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                clockTimer.setText("Time left:" + ((millisUntilFinished/1000) + 1));
            }

            public void onFinish() {
                clockTimer.setText("Game ended!");
            }
        }.start();
    }


    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

}
