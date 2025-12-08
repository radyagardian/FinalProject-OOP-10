package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Player {
    private Vector2 position;
    private float speed;

    // --- SPRITES BAGIAN TUBUH ---
    private Sprite torso;
    private Sprite head;
    private Sprite rifle;
    private Sprite leftFoot, rightFoot; // Kita pakai 2 sprite kaki

    // --- VARIABEL ROTASI & ANIMASI ---
    private Vector3 mouseCoordinates;
    private float currentAngle = 0;

    // Variabel untuk animasi jalan (kaki bergerak)
    private float walkTimer = 0;
    private boolean isMoving = false;

    public Player(float x, float y) {
        position = new Vector2(x, y);
        speed = 200;
        mouseCoordinates = new Vector3();

        // ASSETS
        torso = new Sprite(new Texture("torso.png"));
        head = new Sprite(new Texture("head.png"));
        rifle = new Sprite(new Texture("rifle.png"));

        // satu gambar kaki, dipake buat kiri dan kanan
        Texture footTex = new Texture("foot.png");
        leftFoot = new Sprite(footTex);
        rightFoot = new Sprite(footTex);

        // SCALING
        // kecilkan menjadi 25% (0.25f)
        float scale = 0.4f;
        applyScale(torso, scale);
        applyScale(head, scale);
        applyScale(rifle, scale);
        applyScale(leftFoot, scale);
        applyScale(rightFoot, scale);
    }

    private void applyScale(Sprite s, float scale) {
        s.setSize(s.getWidth() * scale, s.getHeight() * scale);
        s.setOriginCenter();
    }

    public void update(float dt, OrthographicCamera cam) {
        handleInput(dt);
        handleRotation(cam);
        updateBodyParts(dt);
    }

    private void handleInput(float dt) {
        isMoving = false;

        if (Gdx.input.isKeyPressed(Keys.W)) { position.y += speed * dt; isMoving = true; }
        if (Gdx.input.isKeyPressed(Keys.S)) { position.y -= speed * dt; isMoving = true; }
        if (Gdx.input.isKeyPressed(Keys.A)) { position.x -= speed * dt; isMoving = true; }
        if (Gdx.input.isKeyPressed(Keys.D)) { position.x += speed * dt; isMoving = true; }
    }

    private void handleRotation(OrthographicCamera cam) {

        mouseCoordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouseCoordinates);

        float dx = mouseCoordinates.x - position.x;
        float dy = mouseCoordinates.y - position.y;
        float angleRadian = MathUtils.atan2(dy, dx);
        currentAngle = angleRadian * MathUtils.radDeg;
    }

    private void updateBodyParts(float dt) {

        float footOffset = 10f;
        float walkCycleSpeed = 15f;
        float footSwing = 0;

        if (isMoving) {
            walkTimer += dt * walkCycleSpeed;
            footSwing = MathUtils.sin(walkTimer) * 8f;
        } else {
            walkTimer = 0;
            footSwing = 0;
        }

        setSpriteTransform(leftFoot, footSwing, 8);
        setSpriteTransform(rightFoot, -footSwing, -8);

        setSpriteTransform(torso, 0, 0);
        setSpriteTransform(head, 2, 0);
        setSpriteTransform(rifle, 15, -12);
    }


    private void setSpriteTransform(Sprite s, float localX, float localY) {

        float rad = currentAngle * MathUtils.degRad;
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float globalX = position.x + (localX * cos - localY * sin);
        float globalY = position.y + (localX * sin + localY * cos);

        s.setCenter(globalX, globalY);
        s.setRotation(currentAngle);
    }

    public void render(SpriteBatch sb) {
        leftFoot.draw(sb);
        rightFoot.draw(sb);
        torso.draw(sb);
        head.draw(sb);
        rifle.draw(sb);
    }

    public void dispose() {
        torso.getTexture().dispose();
        head.getTexture().dispose();
        rifle.getTexture().dispose();
        leftFoot.getTexture().dispose();
    }
}
