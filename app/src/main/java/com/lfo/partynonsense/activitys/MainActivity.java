package com.lfo.partynonsense.activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lfo.partynonsense.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for permissions for microphone, sensors etc..
        // TODO add more permission-checks
        // If permission is not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    0);

            // Permission OK!
        } else {
            setContentView(R.layout.activity_main);
        }

        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change();
            }
        });
    }

    private void change(){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("nbrOfPlayers", 3); //TODO Get value from dropdown
        startActivity(intent);
    }
}
