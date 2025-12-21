package com.finpro.kel10.Frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.entities.Player;

public class ScoreUIObserver implements Observer {
    private BitmapFont font;
    private SpriteBatch batch; // Batch khusus UI (simpel!)

    private int score;
    private int health;

    public ScoreUIObserver() {
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(1.2f);
        this.batch = new SpriteBatch();

        this.score = 0;
        this.health = 100;
    }


    @Override
    public void onNotify(Player player, String event) {
        this.health = player.getCurrentHealth();
    }


    public void updateScore(int score) {
        this.score = score;
    }

    public void render() {
        batch.begin();

        font.setColor(Color.RED);
        font.draw(batch, "HP: " + health, 20, Gdx.graphics.getHeight() - 20);

        font.setColor(Color.YELLOW);
        font.draw(batch, "Score: " + score, 20, Gdx.graphics.getHeight() - 50);

        batch.end();
    }

    public void dispose() {
        if (font != null) font.dispose();
        if (batch != null) batch.dispose();
    }
}
