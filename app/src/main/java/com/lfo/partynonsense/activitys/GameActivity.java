package com.lfo.partynonsense.activitys;

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

import com.lfo.partynonsense.fragments.AmplitudeGameFragment;
import com.lfo.partynonsense.R;
import com.lfo.partynonsense.fragments.BallGameFragment;
import com.lfo.partynonsense.fragments.RotateGameFragment;
import com.lfo.partynonsense.fragments.WhackAMoleFragment;

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
    private int time;
    private String[] playerNames;
    private int currentPlayer = 0;
    private int currentGame = 0;
    private FragmentManager fragmentManager;
    private ArrayList<Integer> gameFragmentsList = new ArrayList<>();

    private RotateGameFragment rotateGameFragment;
    private AmplitudeGameFragment amplitudeGameFragment;
    private BallGameFragment ballGameFragment;
    private WhackAMoleFragment whackAMoleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        nbrOfPlayers = (Integer) intent.getIntExtra("nbrOfPlayers", -1);
        time = (Integer) intent.getIntExtra("time", -1);
        playerNames = new String[nbrOfPlayers];
        playerNames = intent.getStringArrayExtra("playerNames");
        for (String player : playerNames) {
            playerScore.put(player, 0);
        }
    fragmentManager= getSupportFragmentManager();
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
        tvScore.setText("Score: " + playerScore.get(playerScore.keySet().toArray()[player - 1].toString()));
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
                    whackAMoleFragment = new WhackAMoleFragment();
                    setFragment(whackAMoleFragment, "WHACK_A_MOLE_GAME");
                    break;
                case CONNECTING_DOTS_GAME:
                    ballGameFragment = new BallGameFragment();
                    setFragment(ballGameFragment, "CONNECTING_DOTS_GAME");
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
        new CountDownTimer(time * 1000, 1000) {

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
                whackAMoleFragment.start();
                break;
            case CONNECTING_DOTS_GAME:
                ballGameFragment.start();
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
        int score;
        int newScore;
        switch (gameFragmentsList.get(currentGame - 1)) {
            case ROTATE_GAME:
                rotateGameFragment.stop();
                score = (Integer) playerScore.get(playerScore.keySet().toArray()[currentPlayer - 1].toString());
                newScore = rotateGameFragment.getScore();
                if (newScore < 0) newScore = 0;
                score = score + newScore;
                playerScore.put(playerScore.keySet().toArray()[currentPlayer - 1].toString(), score);
                setTopbarValues(currentPlayer);
                break;
            case WHACK_A_MOLE_GAME:
                whackAMoleFragment.stop();
                score = (Integer) playerScore.get(playerScore.keySet().toArray()[currentPlayer - 1].toString());
                newScore = whackAMoleFragment.getScore();
                if (newScore < 0) newScore = 0;
                score = score + newScore;
                playerScore.put(playerScore.keySet().toArray()[currentPlayer - 1].toString(), score);
                setTopbarValues(currentPlayer);
                break;
            case CONNECTING_DOTS_GAME:
                ballGameFragment.stop();
                score = (Integer) playerScore.get(playerScore.keySet().toArray()[currentPlayer - 1].toString());
                newScore = ballGameFragment.getScore();
                if (newScore < 0) newScore = 0;
                score = score + newScore;
                playerScore.put(playerScore.keySet().toArray()[currentPlayer - 1].toString(), score);
                setTopbarValues(currentPlayer);
                break;
            case SOUND_SENSOR_GAME:
                amplitudeGameFragment.stop();
                score = (Integer) playerScore.get(playerScore.keySet().toArray()[currentPlayer - 1].toString());
                newScore = amplitudeGameFragment.getScore();
                if (newScore < 0) newScore = 0;
                score = score + newScore;
                playerScore.put(playerScore.keySet().toArray()[currentPlayer - 1].toString(), score);
                setTopbarValues(currentPlayer);
                break;
        }
    }
}
