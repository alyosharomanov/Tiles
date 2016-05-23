package game.alyosharomanov.tiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity responsible for showing user's highscores
 */
public class HighScoreActivity extends AppCompatActivity {

    /**
     * Override for onCreate from Android.View
     *
     * @param savedInstanceState previous state of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        View[][] idArr = new View[33][7];
        int[] boardSize = {8, 12, 16, 32};
        int[] numColors = {3, 4, 5, 6};

        idArr[8][3] = findViewById(R.id.highScore83);
        idArr[8][4] = findViewById(R.id.highScore84);
        idArr[8][5] = findViewById(R.id.highScore85);
        idArr[8][6] = findViewById(R.id.highScore86);
        idArr[12][3] = findViewById(R.id.highScore123);
        idArr[12][4] = findViewById(R.id.highScore124);
        idArr[12][5] = findViewById(R.id.highScore125);
        idArr[12][6] = findViewById(R.id.highScore126);
        idArr[16][3] = findViewById(R.id.highScore163);
        idArr[16][4] = findViewById(R.id.highScore164);
        idArr[16][5] = findViewById(R.id.highScore165);
        idArr[16][6] = findViewById(R.id.highScore166);
        idArr[32][3] = findViewById(R.id.highScore323);
        idArr[32][4] = findViewById(R.id.highScore324);
        idArr[32][5] = findViewById(R.id.highScore325);
        idArr[32][6] = findViewById(R.id.highScore326);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        HighScoreManager highScoreManager = new HighScoreManager(sp);

        for (int i: boardSize){
            for (int j: numColors){
                TextView highScore = (TextView) idArr[i][j];
                if (highScoreManager.highScoreExists(i, j)) {
                    highScore.setTextColor(Color.rgb(38,166,154));
                    highScore.setText(String.format(getString(R.string.endgame_old_highscore_text),
                            highScoreManager.getHighScore(i, j)));
                } else {
                    highScore.setTextColor(Color.rgb(158,158,158));
                    highScore.setText("No highscore");
                }
            }
        }

        // Set up the back button
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        return;
    }
}
