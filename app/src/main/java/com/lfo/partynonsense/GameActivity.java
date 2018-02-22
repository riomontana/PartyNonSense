package com.lfo.partynonsense;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

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
        tvScore.setText(playerScore.get(playerScore.keySet().toArray()[player - 1].toString()).toString());
    }

    private void randomGames() {
        Random rand = new Random();
        int game;

        for (int x = 0; x < 3; x++) {
            boolean run = true;
            while (run) {
                game = rand.nextInt(NUMBER_OF_GAMES)+1;
                if (!gameFragmentsList.contains(game)) {
                    gameFragmentsList.add(game);
                    run = false;
                }
            }
        }
        Log.d("LOG", "randomGames: "+ Arrays.toString(gameFragmentsList.toArray()));
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
        if (currentGame < GAMES_TO_PLAY) {
            currentGame++;
            Log.d("LOG", "current Player: " + currentPlayer);
            Log.d("LOG", "current game: " + currentGame);
            switch (gameFragmentsList.get(currentGame - 1)) {
                case ROTATE_GAME:
                    Log.d("LOG", "changeGame: ROTATE GAME");
                    RotateGameFragment fragment = new RotateGameFragment();
                    setFragment(fragment);
                    fragment.setText("Roatate Game");
                    break;
                case WHACK_A_MOLE_GAME:
                    Log.d("LOG", "changeGame: WHACK A MOLE");
                    RotateGameFragment fragment2 = new RotateGameFragment();
                    setFragment(fragment2);
                    fragment2.setText("Whack A Mole Game");
                    break;
                case CONNECTING_DOTS_GAME:
                    Log.d("LOG", "changeGame: CONNECTING DOTS");
                    RotateGameFragment fragment3 = new RotateGameFragment();
                    setFragment(fragment3);
                    fragment3.setText("Connecting Dots Game");
                    break;
                case SOUND_SENSOR_GAME:
                    Log.d("LOG", "changeGame: SOUND SENSOR");
                    RotateGameFragment fragment4 = new RotateGameFragment();
                    setFragment(fragment4);
                    fragment4.setText("Sound Sensor Game");
                    break;
            }
        } else {
            nextPlayer();
        }
    }


    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

}
