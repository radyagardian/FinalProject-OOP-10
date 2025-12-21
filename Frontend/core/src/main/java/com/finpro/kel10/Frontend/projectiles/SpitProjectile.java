package com.finpro.kel10.Frontend.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpitProjectile extends BaseProjectile {

    private static final float WIDTH = 20f;
    private static final float HEIGHT = 20f;

    private Vector2 velocity;
    private float speed = 400f;
    private TextureRegion texture;

    public SpitProjectile(){
        super(); // Panggil constructor BaseProjectile (init position, collider, dll)

        Texture img = new Texture(Gdx.files.internal("zombieSpit.jpg"));
        this.texture = new TextureRegion(img);

        // Atur ukuran collider milik Parent
        this.collider.setSize(WIDTH, HEIGHT);

        this.velocity = new Vector2(0,0);
        // this.active sudah default false dari parent
    }

    public void initialize(Vector2 startPos, Vector2 targetPos){
        this.position.set(startPos);

        // Hitung arah gerak lurus
        velocity.set(targetPos).sub(startPos).nor().scl(speed);

        active = true;
        collider.setPosition(position.x, position.y);
    }

    @Override
    public void update(float delta){
        if(active){
            // Gerak Lurus
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;

            collider.setPosition(position.x, position.y);

            // Logic batas layar (sesuai kode asli Anda)
            if(position.x < -50 || position.x > Gdx.graphics.getWidth() + 50 ||
                position.y < -50 || position.y > Gdx.graphics.getHeight() + 50){
                active = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch){
        if(active){
            batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
        }
    }
}
