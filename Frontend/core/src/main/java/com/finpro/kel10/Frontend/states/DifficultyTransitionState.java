package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.AudioManager;
import com.finpro.kel10.Frontend.strategies.DifficultyStrategy;

public class DifficultyTransitionState extends GameState {
    private PlayingState playingState;
    private DifficultyStrategy newStrategy;
    private BitmapFont font;
    private float timer = 5.3f;
    private String message;
    private GlyphLayout layout;
    private AudioManager audioManager;

    public DifficultyTransitionState(GameStateManager gsm, PlayingState ps, DifficultyStrategy strategy, String message, AudioManager audioManager) {
        super(gsm);
        this.playingState = ps;
        this.newStrategy = strategy;
        this.message = message;
        this.audioManager = audioManager;

        this.font = new BitmapFont();

        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(1.2f);

        this.layout = new GlyphLayout();
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        timer -= dt;
        if (timer <= 0) {
            audioManager.resumeBackgroundMusic();
            playingState.setStrategy(newStrategy);
            gsm.pop();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        playingState.render(sb);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        layout.setText(font, message);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float x = (w - layout.width) / 2f;
        float y = (h + layout.height) / 2f;

        font.draw(sb, layout, x, y);

        sb.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
