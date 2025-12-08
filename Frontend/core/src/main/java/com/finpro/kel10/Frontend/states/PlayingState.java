package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.services.BulletPool;

public class PlayingState extends GameState {
    private Player player;
    private BulletPool bulletPool; // Tambahkan ini

    public PlayingState(GameStateManager gsm) {
        super(gsm);
        player = new Player(400, 300);
        bulletPool = new BulletPool(); // Inisialisasi pool
    }

    @Override
    public void handleInput() {
        // Cek jika tombol kiri mouse ditekan (JustClicked agar tidak nembak beruntun super cepat)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 gunPos = player.getGunTipPosition();
            bulletPool.shoot(gunPos.x, gunPos.y, player.getRotation());

            // muzzle flash
            player.shoot();
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt, cam);
        bulletPool.update(dt); // Update peluru
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        player.render(sb);
        bulletPool.render(sb); // Render peluru

        sb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
        bulletPool.dispose();
    }
}
