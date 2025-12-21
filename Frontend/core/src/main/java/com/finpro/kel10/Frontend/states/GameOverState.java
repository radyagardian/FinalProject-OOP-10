package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.AudioManager;
import com.finpro.kel10.Frontend.GameManager;

public class GameOverState extends GameState {
    private BitmapFont font;
    private int finalScore;
    private int totalKills;
    private GlyphLayout layout;
    private AudioManager audioManager;

    public GameOverState(GameStateManager gsm, AudioManager audioManager) {
        super(gsm);
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);

        layout = new GlyphLayout();

        this.finalScore = GameManager.getInstance().getScore();
        this.totalKills = 0;
        this.audioManager = audioManager;
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            GameManager.getInstance().resetScore();
            audioManager.playBackgroundMusic();

            gsm.set(new PlayingState(gsm, audioManager));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float centerY = h / 2f;

        font.setColor(Color.RED);
        font.getData().setScale(2.5f);
        drawCenteredText(sb, "GAME OVER", w, centerY + 100);

        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        drawCenteredText(sb, "Player: " + GameManager.getInstance().getUsername(), w, centerY + 20);
        drawCenteredText(sb, "Final Score: " + finalScore, w, centerY - 10);
        drawCenteredText(sb, "Zombies Killed: " + totalKills, w, centerY - 40);

        font.setColor(Color.YELLOW);
        drawCenteredText(sb, "Press SPACE to Respawn", w, centerY - 80);

        sb.end();
    }

    private void drawCenteredText(SpriteBatch sb, String text, float screenWidth, float y) {
        layout.setText(font, text);
        float x = (screenWidth - layout.width) / 2f;
        font.draw(sb, layout, x, y);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
