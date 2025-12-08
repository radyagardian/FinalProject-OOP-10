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
    // --- KONFIGURASI POSISI ---
    private float scale = 0.4f;
    private float RIFLE_FORWARD = 40f;
    private float RIFLE_SIDE = -45f;
    private float GUN_LENGTH = 85f;

    // --- VARIABLES ---
    private Vector2 position;
    private float speed;
    private Sprite torso, head, rifle, leftFoot, rightFoot;

    // UBAH INI: Jadi Array untuk menampung banyak variasi flash
    private Sprite[] muzzleFlashes;
    private int activeFlashIndex = 0; // Index flash yang sedang aktif

    private Vector3 mouseCoordinates;
    private float currentAngle = 0;
    private float walkTimer = 0;
    private boolean isMoving = false;
    private float flashTimer = 0f;

    public Player(float x, float y) {
        position = new Vector2(x, y);
        speed = 200;
        mouseCoordinates = new Vector3();

        // LOAD ASSETS
        torso = new Sprite(new Texture("torso.png"));
        head = new Sprite(new Texture("head.png"));
        rifle = new Sprite(new Texture("rifle.png"));

        Texture footTex = new Texture("foot.png");
        leftFoot = new Sprite(footTex);
        rightFoot = new Sprite(footTex);

        muzzleFlashes = new Sprite[2];
        muzzleFlashes[0] = new Sprite(new Texture("muzzle_flash_01.png"));
        muzzleFlashes[1] = new Sprite(new Texture("muzzle_flash_02.png"));

        // APPLY SCALE
        applyScale(torso, scale);
        applyScale(head, scale);
        applyScale(rifle, scale);
        applyScale(leftFoot, scale);
        applyScale(rightFoot, scale);

        // Scale semua flash dalam array
        for (Sprite flash : muzzleFlashes) {
            applyScale(flash, scale * 0.06f);
        }
    }

    private void applyScale(Sprite s, float scale) {
        s.setSize(s.getWidth() * scale, s.getHeight() * scale);
        s.setOriginCenter();
    }

    public void update(float dt, OrthographicCamera cam) {
        handleInput(dt);
        handleRotation(cam);
        updateBodyParts(dt);

        if (flashTimer > 0) {
            flashTimer -= dt;
            if (flashTimer < 0) flashTimer = 0;
        }
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
        currentAngle = MathUtils.atan2(dy, dx) * MathUtils.radDeg;
    }

    private void updateBodyParts(float dt) {
        float walkCycleSpeed = 15f;
        float footSwing = 0;
        float swingRange = 32f * scale;

        if (isMoving) {
            walkTimer += dt * walkCycleSpeed;
            footSwing = MathUtils.sin(walkTimer) * swingRange;
        } else {
            walkTimer = 0;
            footSwing = 0;
        }

        setSpriteTransform(leftFoot, footSwing, 8f * scale);
        setSpriteTransform(rightFoot, -footSwing, -8f * scale);
        setSpriteTransform(torso, 0, 0);
        setSpriteTransform(head, 2f * scale, 0);
        setSpriteTransform(rifle, RIFLE_FORWARD * scale, RIFLE_SIDE * scale);
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

        // random flash
        if (flashTimer > 0) {
            Vector2 tip = getGunTipPosition();

            Sprite activeFlash = muzzleFlashes[activeFlashIndex];

            activeFlash.setCenter(tip.x, tip.y);
            activeFlash.setRotation(currentAngle);
            activeFlash.draw(sb);
        }
    }

    public void shoot() {
        flashTimer = 0.05f;

        // Pilih angka acak: 0 atau 1 untuk pemilihan sprite flash yang digunakan
        activeFlashIndex = MathUtils.random(0, 1);
    }

    public Vector2 getGunTipPosition() {
        float rad = currentAngle * MathUtils.degRad;
        float totalForward = (RIFLE_FORWARD + GUN_LENGTH) * scale;
        float totalSide = RIFLE_SIDE * scale;

        float tipX = position.x + (MathUtils.cos(rad) * totalForward) - (MathUtils.sin(rad) * totalSide);
        float tipY = position.y + (MathUtils.sin(rad) * totalForward) + (MathUtils.cos(rad) * totalSide);

        return new Vector2(tipX, tipY);
    }

    public float getRotation() { return currentAngle; }

    public void dispose() {
        torso.getTexture().dispose();
        head.getTexture().dispose();
        rifle.getTexture().dispose();
        leftFoot.getTexture().dispose();

        for (Sprite flash : muzzleFlashes) {
            flash.getTexture().dispose();
        }
    }
}
