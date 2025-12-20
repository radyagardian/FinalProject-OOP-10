package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.Main;

public abstract class GameState {
    protected GameStateManager gsm;
    protected OrthographicCamera cam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
    }

    protected GameState() {
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
