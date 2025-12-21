package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseItem {
    protected Vector2 position;
    protected Sprite sprite;
    protected boolean active;
    protected Rectangle bounds;

    public BaseItem() {
        position = new Vector2();
        active = false;
        bounds = new Rectangle();
    }

    // Setiap item wajib punya efek saat diambil player
    public abstract void onPickup(Player player);

    public void spawn(float x, float y) {
        position.set(x, y);
        sprite.setPosition(x, y);
        bounds.setPosition(x, y);
        active = true;
    }

    public void render(SpriteBatch sb) {
        if (active) sprite.draw(sb);
    }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public Rectangle getBounds() { return bounds; }
    public void update(float dt) {}

    public void dispose() {
        if (sprite.getTexture() != null) sprite.getTexture().dispose();
    }
}
