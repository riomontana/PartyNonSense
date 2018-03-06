package com.lfo.partynonsense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * GUI for testing Database
 * Implemented methods can be used later
 * @author Linus Forsberg
 */
public class DummyDBActivity extends AppCompatActivity {

    private EditText inputPlayer;
    private DBHelper dbHelper;
    private int playerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_db);
        dbHelper = new DBHelper(this);
        inputPlayer = findViewById(R.id.inputPlayer);
    }

    /**
     * Button handler
     * adds player to database and gets the player ID from the database
     * @param view
     */
    public void addPlayerToDB(View view) {
        String playerName = inputPlayer.getText().toString();
        dbHelper.addPlayer(playerName);
        playerID = dbHelper.getPlayerID(playerName);
        Log.d("playerid", String.valueOf(playerID));
    }

    /**
     * button handler
     * adds score to database
     * @param view
     */
    public void addScoreToDB(View view) {
        int score = 1000;
        dbHelper.addScore(playerID, score);
    }

    /**
     * button handler
     * resets database
     * @param view
     */
    public void resetDB(View view) {
        dbHelper.resetDataBase();
    }

    /**
     * gets highscore list from database and prints it to the log window
     * @param view
     */
    public void getHighscoreListFromDB(View view) {
        ArrayList<HighscoreModel> highscoreList = dbHelper.readHighscoreEntries();
        for(HighscoreModel entries : highscoreList)
            Log.d(entries.player, String.valueOf(entries.score));
    }
}
