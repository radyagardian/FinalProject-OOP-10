package com.finpro.kel10.Frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.states.GameStateManager;
import com.finpro.kel10.Frontend.states.PlayingState;
import com.finpro.kel10.Frontend.AudioManager;

public class Main extends ApplicationAdapter {
    // Definisi ukuran layar virtual
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Lead 4 Dead";

    private GameStateManager gsm;
    private SpriteBatch batch;
    private AudioManager audioManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);

        gsm.push(new PlayingState(gsm));

        //buat backgroun music
        audioManager = new AudioManager();
        audioManager.playBackgroundMusic();

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        audioManager.dispose();
    }

}
