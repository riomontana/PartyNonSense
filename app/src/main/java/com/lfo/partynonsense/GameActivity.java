package com.lfo.partynonsense;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameActivity extends AppCompatActivity {

    private TextView tvPlayer, clockTimer, tvScore;
    private HashMap playerScore = new LinkedHashMap<String, Integer>();
    private int nbrOfPlayers = 0;
    private int currentPlayer = 0;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //TODO Testvalues REMOVE
        playerScore.put("Player 1", 10);
        playerScore.put("Player 2", 20);
        playerScore.put("Player 3", 15);
        //TODO Testvalues REMOVE

        initTopbar();
        Intent intent = getIntent();
        nbrOfPlayers = intent.getIntExtra("nbrOfPlayers", -1);
        for (int x = 0; x < nbrOfPlayers; x++) {
            currentPlayer++;
            setTopbarValues(currentPlayer);


        }
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

    public void setFragment(Fragment fragment, String tag, boolean backstack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id., fragment, tag);
        fragmentTransaction.commit();
    }

}
