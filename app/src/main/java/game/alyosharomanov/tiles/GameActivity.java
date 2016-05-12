package game.alyosharomanov.tiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Main activity that hosts the whole app (a.k.a. onCreate modification)
 */
public class GameActivity extends AppCompatActivity implements EndGameDialog.EndGameDialogListener{

    private final int SETTINGS_INT = 1;

    private Game game;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private BoardView boardView;
    private TextView numMoves;

    private int prevColor;

    private boolean gameIsFinished;

    private Paint paints[];

    /**
     * Override for onCreate from Android.View
     *
     * @param savedInstanceState previous state of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize the SharedPreferences and SharedPreferences editor
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spEditor = sp.edit();

        // Get the BoardView
        boardView = (BoardView) findViewById(R.id.boardLayout);

        // Initialize the paints array and pass it to the FloodView
        initPaints();
        boardView. setPaints(paints);

        //create Settings button and launch its class
        ImageView settingsButton = (ImageView) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent launchSettingsIntent = new Intent(GameActivity.this, Settings.class);
                                                  startActivityForResult(launchSettingsIntent, SETTINGS_INT);
                                              }
                                          }
        );

        //create Info button and launch its class
        ImageView infoButton = (ImageView) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchInfoIntent = new Intent(GameActivity.this, InfoActivity.class);
                startActivity(launchInfoIntent);
            }
        });

        //create Social button and launch its class
        ImageView socialButton = (ImageView) findViewById(R.id.socialButton);
        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSocialIntent = new Intent(GameActivity.this, SocialActivity.class);
                startActivity(launchSocialIntent);
            }
        });

        //create HighScore button and launch its class
        ImageView highScoreButton = (ImageView) findViewById(R.id.highScore);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchHighScoreIntent = new Intent(GameActivity.this, HighScoreActivity.class);
                startActivity(launchHighScoreIntent);
            }
        });

        // Get the steps for showing them on the board
        numMoves = (TextView) findViewById(R.id.numMoves);

        // Set up a new game
        newGame();
    }

    /**
     * Returns default boardSize as defined by the xml
     *
     * @return boardSize
     */
    private int getBoardSize() {
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        if (!sp.contains("board_size")) {
            spEditor.putInt("board_size", defaultBoardSize);
            spEditor.apply();
        }
        return sp.getInt("board_size", defaultBoardSize);
    }

    /**
     * Returns default numColors as defined by the xml
     *
     * @return numColors
     */
    private int getNumColors() {
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (!sp.contains("num_colors")) {
            spEditor.putInt("num_colors", defaultNumColors);
            spEditor.apply();
        }
        return sp.getInt("num_colors", defaultNumColors);
    }

    /**
     * Initialize paints for the board as defined by xml
     */
    private void initPaints() {
        int[] colors;
        if (sp.getBoolean("use_material", false)){
            colors = getResources().getIntArray(R.array.boardColorSchemeMaterial);
        } else {
            colors = getResources().getIntArray(R.array.boardColorScheme);
        }

        paints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            paints[i] = new Paint();
            paints[i].setColor(colors[i]);
        }
        return;
    }

    /**
     * Starts a new game with current Settings
     */
    private void newGame() {
        game = new Game(getBoardSize(), getNumColors());
        gameIsFinished = false;
        prevColor = game.getColor(0,0);

        layoutColorButtons();

        numMoves.setText(game.getSteps() + " / " + game.getMaxSteps());
        boardView.setBoardSize(getBoardSize());
        boardView.draw(game);
    }

    /**
     * Create the color buttons at bottom of screen
     */
    private void layoutColorButtons() {
        // Add color buttons
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        buttonLayout.removeAllViews();
        int buttonPadding = (int) getResources().getDimension(R.dimen.color_button_padding);
        for (int i = 0; i < getNumColors(); i++) {
            final int localI = i;
            ButtonsView newButton = new ButtonsView(this);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(GameActivity.this, R.anim.button_anim));
                    if (localI != prevColor) {
                        doGame(localI);
                    }
                }
            });
            newButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            newButton.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);

            newButton.setColor(paints[i].getColor());
            buttonLayout.addView(newButton);
        }
        return;
    }

    /**
     * Default onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_INT) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                // Only start a new game if the settings have been changed
                if (extras.getBoolean("gameSettingsChanged")) {
                    newGame();
                }
                if (extras.getBoolean("colorSettingsChanged")) {
                    initPaints();
                    boardView.setPaints(paints);
                    layoutColorButtons();
                }
            }
        }
    }

    /**
     * Class responsible for executing all events when an input is given
     * @param color
     */
    private void doGame(int color) {
        if (gameIsFinished || game.getSteps() >= game.getMaxSteps()) {
            return;
        }

        game.fillCycle(color, false);
        boardView.draw(game);
        prevColor = color;
        numMoves.setText(game.getSteps() + " / " + game.getMaxSteps());

        if (game.checkWin() || game.getSteps() == game.getMaxSteps()) {
            gameIsFinished = true;
            showEndGameDialog();
        }

        return;
    }

    /**
     * Action to execute when newGameIsClicked
     */
    public void onNewGameClick() {
        newGame();
        return;
    }

    /**
     * Actions to execute when endGameDialog needs to appear
     */
    private void showEndGameDialog() {
        DialogFragment endGameDialog = new EndGameDialog();
        Bundle args = new Bundle();
        args.putInt("steps", game.getSteps());
        args.putBoolean("game_won", game.checkWin());
        args.putString("seed", game.getSeed());
        endGameDialog.setArguments(args);
        endGameDialog.show(getSupportFragmentManager(), "EndGameDialog");
        return;
    }
}
