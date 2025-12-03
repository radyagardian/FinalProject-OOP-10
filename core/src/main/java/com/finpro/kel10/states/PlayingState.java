package com.finpro.kel10.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.entities.Player;

public class PlayingState extends GameState {
    private Player player;

    public PlayingState(GameStateManager gsm) {
        super(gsm);
        player = new Player(400, 300);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        player.update(dt, cam);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        player.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
