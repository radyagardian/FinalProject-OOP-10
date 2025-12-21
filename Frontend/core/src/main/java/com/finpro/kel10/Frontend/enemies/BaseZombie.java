package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.GameManager;
import com.finpro.kel10.Frontend.entities.Player;

public abstract class BaseZombie {
    protected Vector2 position;
    protected Sprite sprite;
    protected Rectangle collider;
    protected float speed;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int scoreValue;
    protected boolean active;
    protected float attackTimer;
    protected float attCd;

    public BaseZombie(Vector2 startPos){
        this.position = new Vector2(startPos);
        this.sprite = new Sprite();
        this.collider = new Rectangle();
        this.active = false;
        this.attackTimer = 0f;
    }

    public abstract void botBehavior(float delta, Player player);

    public void update(float delta, Player player){
        botBehavior(delta, player);
        attackTimer -= delta;
        collider.setPosition(sprite.getX(), sprite.getY());
    }

    public void render(SpriteBatch batch){
        if(active){
            sprite.draw(batch);
        }
    }

    public void takeDamage(int amount){
        health -= amount;
        if(health <= 0){
            onDeath();
        }
    }

    public void onDeath(){
        active = false;
        GameManager.getInstance().addScore(scoreValue);
        GameManager.getInstance().addKill();
    }

    public void initialize(float x, float y){
        position.set(x, y);
        sprite.setPosition(x, y);
        collider.setSize(sprite.getWidth(), sprite.getHeight());
        collider.setPosition(x, y);
        this.active = true;
        health = maxHealth;
        active = true;
        attackTimer = 0;
    }

    public int getDamage(){
        return damage;
    }

    public Rectangle getCollider(){
        return collider;
    }

    public boolean isActive(){
        return active;
    }
}
