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
 * @author Jimmy Åkesson
 *         <p>
 *         edited by Linus Forsberg
 *         Added count down timer in StartCountDown method
 *         Added startGame and endGame methods.
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

    private RotateGameFragment rotateGameFragment;
    private AmplitudeGameFragment amplitudeGameFragment;
    // todo lägg till WHACK A MOLE och CONNECTING DOTS som instansvariabler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        if (currentGame < GAMES_TO_PLAY) {
            currentGame++;
            switch (gameFragmentsList.get(currentGame - 1)) {
                case ROTATE_GAME:
                    rotateGameFragment = new RotateGameFragment();
                    setFragment(rotateGameFragment, "ROTATE_GAME");
                    break;
                case WHACK_A_MOLE_GAME:
                    TestFragment fragment2 = new TestFragment();
                    setFragment(fragment2, "WHACK_A_MOLE_GAME");
                    break;
                case CONNECTING_DOTS_GAME:
                    TestFragment fragment3 = new TestFragment();
                    setFragment(fragment3, "CONNECTING_DOTS_GAME");
                    break;
                case SOUND_SENSOR_GAME:
                    amplitudeGameFragment = new AmplitudeGameFragment();
                    setFragment(amplitudeGameFragment, "SOUND_SENSOR_GAME");
                    break;
            }
        } else {
            nextPlayer();
        }
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Start count down timer and update user interface
     */
    public void startCountDownTimer() {
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                clockTimer.setText("Time left:" + ((millisUntilFinished / 1000) + 1));
            }

            public void onFinish() {
                clockTimer.setText("Game ended!");
                endGame();
            }
        }.start();
        startGame();
    }

    /**
     * Calls the start method in current game fragment
     */
    private void startGame() {
        switch (gameFragmentsList.get(currentGame - 1)) {
            case ROTATE_GAME:
                rotateGameFragment.start();
                break;
            case WHACK_A_MOLE_GAME:
                // todo Add call for start game for WHACK A MOLE
                break;
            case CONNECTING_DOTS_GAME:
                // todo Add call for start game for CONNECTING DOTS
                break;
            case SOUND_SENSOR_GAME:
                amplitudeGameFragment.start();
                break;
        }
    }

    /**
     * Calls the end method in current game fragment
     */
    private void endGame() {
        switch (gameFragmentsList.get(currentGame - 1)) {
            case ROTATE_GAME:
                rotateGameFragment.stop();
                rotateGameFragment.getScore();
                break;
            case WHACK_A_MOLE_GAME:
                // todo Add calls for stop game and get score for WHACK A MOLE
                break;
            case CONNECTING_DOTS_GAME:
                // todo Add calls for stop game and get score for CONNECTING DOTS
                break;
            case SOUND_SENSOR_GAME:
                amplitudeGameFragment.stop();
                amplitudeGameFragment.getScore();
                break;
        }
    }
}
