package com.lfo.partynonsense;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * TODO Remove this Activity when the time is right
 * This activity is created for temporary use for display of AmplitudeGameFragment
 * Can be deleted when proper FragmentActivity is in place
 */

public class DummyFragmentActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If permission is not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.VIBRATE},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            // Permission OK!
        }
        setContentView(R.layout.activity_dummy_fragment);
    }
}



