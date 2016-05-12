package game.alyosharomanov.tiles;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Dialog Fragment that is displayed to the user upon a win or loss.
 */
public class EndGameDialog extends DialogFragment{

    public interface EndGameDialogListener{
        void onNewGameClick();
    }

    EndGameDialogListener listener;

    /**
     * Override for onCreateDialog in Android.onCreateDialog
     *
     * @param savedInstanceState saved state of the board to return to
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get steps and maxSteps from the arguments
        int steps = getArguments().getInt("steps");
        boolean gameWon = getArguments().getBoolean("game_won");

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_endgame, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set up the dialog's title
        TextView endgameText = (TextView) layout.findViewById(R.id.endGameTitle);
        if (gameWon) {
            endgameText.setText(getString(R.string.endgame_win_title));
        } else {
            endgameText.setText(getString(R.string.endgame_lose_title));
        }

        // Set up dialog's other text views
        TextView endgameTextView = (TextView) layout.findViewById(R.id.endGameText);
        TextView highScoreTextView = (TextView) layout.findViewById(R.id.highScoreText);
        ImageView highScoreMedalImageView = (ImageView) layout.findViewById(R.id.highScoreMedalImageView);

        //Initialize prefernces
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        HighScoreManager highScoreManager = new HighScoreManager(sp);

        int boardSize = sp.getInt("board_size", -1);
        int numColors = sp.getInt("num_colors", -1);

        //Actions carried out by win/loss
        if (gameWon) {
            String stepsString = String.format(getString(R.string.endgame_win_text),
                    steps);
            endgameTextView.setText(stepsString);

            if (highScoreManager.isHighScore(boardSize, numColors, steps)) {
                highScoreManager.setHighScore(boardSize, numColors, steps);
                highScoreTextView.setText(String.format(getString(R.string.endgame_new_highscore_text),
                        steps));
                highScoreTextView.setTypeface(null, Typeface.BOLD);
            } else {
                highScoreTextView.setText(String.format(getString(R.string.endgame_old_highscore_text),
                        highScoreManager.getHighScore(boardSize, numColors)));
                highScoreMedalImageView.setVisibility(View.GONE);
            }

        } else {
            endgameTextView.setVisibility(View.GONE);
            if (highScoreManager.highScoreExists(boardSize, numColors)) {
                highScoreTextView.setText(String.format(getString(R.string.endgame_old_highscore_text),
                        highScoreManager.getHighScore(boardSize, numColors)));
                highScoreMedalImageView.setVisibility(View.GONE);
            } else {
                highScoreTextView.setVisibility(View.GONE);
                highScoreMedalImageView.setVisibility(View.GONE);
            }

        }

        // Set up the new game button
        Button newGameButton = (Button) layout.findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewGameClick();
                dismiss();
            }
        });

        //adview
        AdView mAdView = (AdView) layout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //returns the dialogFragment
        return dialog;
    }

    /**
     * Safety net for NullPointerException
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EndGameDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EndGameDialogListener");
        }
    }


}
