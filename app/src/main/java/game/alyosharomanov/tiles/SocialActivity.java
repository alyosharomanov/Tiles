package game.alyosharomanov.tiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Activity responsible for social aspects like ratings
 */
public class SocialActivity extends AppCompatActivity {

    /**
     * Override for onCreate from Android.View
     *
     * @param savedInstanceState previous state of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

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
