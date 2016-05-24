package game.alyosharomanov.tiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        Button rateButton = (Button) findViewById(R.id.rateButton);//button name in xml file
        rateButton.setOnClickListener(rate); // on button click listener

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

    private Button.OnClickListener rate = new Button.OnClickListener() {
        public void onClick(View v) {

            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=game.alyosharomanov.tiles");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);


        }
    };
}
