package game.alyosharomanov.tiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Activity responsible for game and application settings
 */
public class Settings extends AppCompatActivity {
    CheckBox materialCheckBox;
    int[] boardSizeChoices, numColorsChoices;
    private int selectedBoardSize, selectedNumColors;

    /**
     * Override for onCreate from Android.View
     *
     * @param savedInstanceState previous state of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the board size RadioGroup
        RadioGroup boardSizeRadioGroup = (RadioGroup) findViewById(R.id.boardSizeRadioGroup);
        boardSizeChoices = getResources().getIntArray(R.array.boardSizeChoices);
        selectedBoardSize = sp.getInt("board_size",
                getResources().getInteger(R.integer.default_board_size));
        for (final int bs : boardSizeChoices) {
            RadioButton currRadioButton = new RadioButton(this);
            currRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.small_text_size));
            currRadioButton.setText(String.format("%dx%d", bs, bs));
            boardSizeRadioGroup.addView(currRadioButton);
            if (bs == selectedBoardSize) {
                boardSizeRadioGroup.check(currRadioButton.getId());
            }
            currRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.this.setSelectedBoardSize(bs);
                }
            });
        }

        // Set up the num colors RadioGroup
        RadioGroup numColorsRadioGroup = (RadioGroup) findViewById(R.id.numColorsRadioGroup);
        numColorsChoices = getResources().getIntArray(R.array.numColorsChoices);
        selectedNumColors = sp.getInt("num_colors",
                getResources().getInteger(R.integer.default_num_colors));
        for (final int nc : numColorsChoices) {
            RadioButton currRadioButton = new RadioButton(this);
            currRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.small_text_size));
            currRadioButton.setText(Integer.toString(nc));
            numColorsRadioGroup.addView(currRadioButton);
            if (nc == selectedNumColors) {
                numColorsRadioGroup.check(currRadioButton.getId());
            }
            currRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.this.setSelectedNumColors(nc);
                }
            });
        }

        // Set up the material scheme checkbox
        materialCheckBox = (CheckBox) findViewById(R.id.materialColorsCheckBox);
        materialCheckBox.setChecked(sp.getBoolean("use_material", true));


        // Set up the play button
        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = newGame();
                setResult(RESULT_OK, s);
                finish();
            }
        });
    }

    /**
     * updates the board size
     *
     * @param boardSize of board
     */
    private void setSelectedBoardSize(int boardSize) {
        this.selectedBoardSize = boardSize;
    }

    /**
     * updaes the number of colors
     *
     * @param numColors of colors
     */
    private void setSelectedNumColors(int numColors) {
        this.selectedNumColors = numColors;
    }

    /**
     * Creates a new game with current settings
     *
     * @return Intent
     */
    private  Intent newGame(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();
        Intent dataIntent = new Intent();
        dataIntent.putExtra("gameSettingsChanged", false);
        dataIntent.putExtra("colorSettingsChanged", false);

        // Update boardSize
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        dataIntent.putExtra("gameSettingsChanged", true);
        spEditor.putInt("board_size", selectedBoardSize);

        // Update number of colors
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        dataIntent.putExtra("gameSettingsChanged", true);
        spEditor.putInt("num_colors", selectedNumColors);

        // Update whether or not to use the old color scheme
        boolean selectedMaterial = materialCheckBox.isChecked();
        if (selectedMaterial != sp.getBoolean("use_material", false)) {
            dataIntent.putExtra("colorSettingsChanged", true);
            spEditor.putBoolean("use_material", selectedMaterial);
        }

        spEditor.apply();
        return dataIntent;
    }
}
