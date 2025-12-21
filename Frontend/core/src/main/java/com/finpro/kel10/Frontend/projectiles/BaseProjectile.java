package com.finpro.kel10.Frontend.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseProjectile {
    protected Vector2 position;
    protected Rectangle collider;
    protected boolean active;
    protected int damage;

    public BaseProjectile() {
        this.position = new Vector2();
        this.collider = new Rectangle();
        this.active = false;
        this.damage = 10; // Default damage
    }

    // Method wajib untuk semua peluru
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);

    // Getters & Setters
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Rectangle getCollider() { return collider; }

    public int getDamage() { return damage; }

    public Vector2 getPosition() { return position; }
}
