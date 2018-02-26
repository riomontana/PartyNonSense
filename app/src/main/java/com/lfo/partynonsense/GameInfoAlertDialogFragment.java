package com.lfo.partynonsense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Displays info about current game before the game is started.
 *
 * @author Linus Forsberg
 */

public class GameInfoAlertDialogFragment extends DialogFragment {

    /**
     * Create alert dialog fragment
     * @param savedInstanceState
     * @return
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String gameName = (String) getArguments().get("gameName");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(gameName)
                .setMessage("Information about the game will be displayed here")
                .setPositiveButton("START GAME!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((GameActivity)getActivity()).startCountDownTimer();
                    }
                });
        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }
}
