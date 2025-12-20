package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private Sprite sprite;
    private boolean active;
    private float speed = 1000f; // Kecepatan peluru
    private Rectangle collider;

    public Bullet() {
        position = new Vector2();
        velocity = new Vector2();
        active = false;

        Texture tex = new Texture("bullet.png");
        sprite = new Sprite(tex);
        sprite.setSize(10, 10);
        sprite.setOriginCenter();

        this.collider = new Rectangle(0, 0, 10, 10);
    }


    public void init(float x, float y, float angleDeg) {
        position.set(x, y);

        // Hitung vektor kecepatan berdasarkan sudut rotasi
        float angleRad = angleDeg * MathUtils.degRad;
        velocity.x = MathUtils.cos(angleRad) * speed;
        velocity.y = MathUtils.sin(angleRad) * speed;

        sprite.setRotation(angleDeg);
        active = true;
        collider.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);
    }

    public void update(float dt) {
        if (!active) return;

        position.mulAdd(velocity, dt);
        sprite.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);

        // Deaktifkan jika keluar layar
        if (position.x < -100 || position.x > 2000 || position.y < -100 || position.y > 2000) {
            active = false;
        }
        collider.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);
    }

    public void render(SpriteBatch sb) {
        if (active) {
            sprite.draw(sb);
        }
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public Rectangle getCollider(){
        return collider;
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
