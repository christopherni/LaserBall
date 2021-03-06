package com.chrisni.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.chrisni.game.LaserBall;

/**
 * Created by Chris on 5/23/2017.
 */
public class GameOverScreen implements Screen {

    private final LaserBall game;
    private final int NUM_HIGHSCORES = 5;
    private Preferences prefs;
    private Stage stage;
    private int score;
    private Skin skin;

    static OrthographicCamera camera;


    public GameOverScreen(final LaserBall laserBall, Stage mStage, Skin mSkin, Preferences mPrefs) {
        this.game = laserBall;

        this.prefs = mPrefs;
        this.skin = mSkin;
        this.stage = mStage;

        this.score = prefs.getInteger("gameScore");
        prefs.putInteger("coins", prefs.getInteger("coins", 0) + this.score);

        final TextButton playButton = new TextButton("Play Again", skin, "default");
        playButton.setWidth(150f);
        playButton.setHeight(64f);
        playButton.setPosition(0, 100);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        stage.clear();
                        game.setScreen(new GameScreen(game, stage, skin, prefs));
                    }});
            }
        });

        final TextButton quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(150f);
        quitButton.setHeight(64f);
        quitButton.setPosition(320, 100);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        dispose();
                        Gdx.app.exit();
                    }});
            }
        });

        final TextButton optionButton = new TextButton("Options", skin, "default");
        optionButton.setWidth(150f);
        optionButton.setHeight(64f);
        optionButton.setPosition(160, 100);
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        stage.clear();
                        game.setScreen(new OptionsScreen(game, stage, skin, prefs, GameOverScreen.this));
                    }});
            }
        });

        Label loseLabel = new Label("You lost with a score of " + score + "!!!", skin, "title");
        loseLabel.setPosition(40, 550);

        Label highScoreLabel = new Label("High Scores", skin, "title");
        highScoreLabel.setPosition(100, 500);

        setHighScores();

        Image title = new Image(skin.getAtlas().findRegion("label"));
        title.setWidth(170);
        title.setHeight(170);
        title.setPosition(150, 600);

        Label titleLabel = new Label("defeat", skin, "title");
        titleLabel.setPosition(title.getX() + title.getWidth() / 2, title.getY() + title.getHeight() / 2, Align.center);

        stage.addActor(playButton);
        stage.addActor(optionButton);
        stage.addActor(quitButton);
        stage.addActor(loseLabel);
        stage.addActor(highScoreLabel);
        stage.addActor(title);
        stage.addActor(titleLabel);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void setHighScores() {
        int[] scores = new int[NUM_HIGHSCORES + 1];
        for (int i = 0; i < NUM_HIGHSCORES; i++) {
            String key = "highscore" + i;
            scores[i] = prefs.getInteger(key, -1);
        }
        scores[NUM_HIGHSCORES] = score;
        insertionSort(scores);
        for (int i = 0; i < NUM_HIGHSCORES; i++) {
            String key = "highscore" + i;
            int curr = scores[i];
            if (curr >= 0) {
                prefs.putInteger(key, curr);
            }
            String currScore = (i + 1) + ". " + ((scores[i] >= 0) ? scores[i] : "--");
            Label scoreLabel = new Label(currScore, skin, "title");
            scoreLabel.setPosition(100, 450 - 50 * i);
            stage.addActor(scoreLabel);
        }
        prefs.flush();
    }


    private void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] < temp) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = temp;
        }
    }
}
