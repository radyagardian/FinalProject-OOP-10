package com.finpro.kel10.Frontend.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpitProjectile {
    private static final float WIDTH = 20f;
    private static final float HEIGHT = 20f;
    private Vector2 position;
    private Vector2 velocity;
    private float speed =  400f;
    private boolean active;
    private TextureRegion texture;
    private Rectangle collider;
    private int damage;

    public SpitProjectile(){
        Texture img = new Texture(Gdx.files.internal("zombieSpit.png"));
        this.texture = new TextureRegion(img);
        this.collider = new Rectangle(0, 0, WIDTH, HEIGHT);
        this.position = new Vector2();
        this.velocity = new Vector2(0,0);
        this.active = false;
    }

    public void initialize(Vector2 startPos, Vector2 targetPos){
        this.position = startPos;
        velocity.set(targetPos).sub(startPos).nor().scl(speed);
        active = true;
    }

    public void update(float delta){
        if(active){
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            collider.setPosition(position.x, position.y);
            if(position.x < -50 || position.x > Gdx.graphics.getWidth() + 50 ||
                position.y < -50 || position.y > Gdx.graphics.getHeight() + 50){
                active = false;
            }
        }
    }

    public void render(SpriteBatch batch){
        if(active){
            batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
        }
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
