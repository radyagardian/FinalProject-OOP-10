package com.finpro.kel10.Frontend.services;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.kel10.Frontend.entities.Bullet;
import java.util.ArrayList;
import java.util.List;

public class BulletPool {
    private List<Bullet> bullets;
    private final int POOL_SIZE = 50;

    public BulletPool() {
        bullets = new ArrayList<>();

        for (int i = 0; i < POOL_SIZE; i++) {
            bullets.add(new Bullet());
        }
    }


    public void shoot(float x, float y, float angle) {
        // Cari peluru yang tidak aktif, lalu aktifkan
        for (Bullet b : bullets) {
            if (!b.isActive()) {
                b.init(x, y, angle);
                break; // aktifkan satu peluru aja
            }
        }
    }

    public void update(float dt) {
        for (Bullet b : bullets) {
            if (b.isActive()) {
                b.update(dt);
            }
        }
    }

    public void render(SpriteBatch sb) {
        for (Bullet b : bullets) {
            if (b.isActive()) {
                b.render(sb);
            }
        }
    }

    public void dispose() {
        for(Bullet b : bullets) {
            b.dispose();
        }
    }
}
