package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.finpro.kel10.Frontend.observers.Observer;
import com.finpro.kel10.Frontend.observers.Subject;
import com.finpro.kel10.Frontend.AudioManager;

import java.util.ArrayList;
import java.util.List;

public class Player implements Subject {
    // --- KONFIGURASI POSISI ---
    private float scale = 0.3f;
    private float RIFLE_FORWARD = 40f;
    private float RIFLE_SIDE = -45f;
    private float fireRate = 0.175f;
    private float fireTimer = 0f;
    private float GUN_LENGTH = 85f;
    private List<Observer> observers = new ArrayList<>();

    // --- TUNING FLASH (BARU) ---
    // Ubah angka ini untuk memajukan/memundurkan flash
    // Positif = Maju menjauhi laras, Negatif = Mundur mendekati laras
    private float FLASH_FORWARD_ADJUSTMENT = 25f;

    // --- POSISI TANGAN & LENGAN (TIDAK DIUBAH) ---

    // KANAN (Trigger)
    private float HAND_RIGHT_FORWARD = 35f;
    private float HAND_RIGHT_SIDE = -45f;

    private float ARM_RIGHT_FORWARD = 10f;
    private float ARM_RIGHT_SIDE = -35f;
    private float ARM_RIGHT_ANGLE = -10f;

    // KIRI (Steady)
    private float HAND_LEFT_FORWARD = 85f;
    private float HAND_LEFT_SIDE = -43f;

    // Lengan Kiri (Bahu ke Siku)
    private float ARM_LEFT_FORWARD = 30f;
    private float ARM_LEFT_SIDE = 35f;
    private float ARM_LEFT_ANGLE = -50f;

    // Lengan Bawah Kiri (Siku ke Tangan)
    private float FOREARM_LEFT_FORWARD = 65f;
    private float FOREARM_LEFT_SIDE = -10f;
    private float FOREARM_LEFT_ANGLE = -55f;

    // --- VARIABLES ---
    private Vector2 position;
    private Vector2 startPosition;
    private float speed;
    private Sprite torso, head, leftFoot, rightFoot;
    private Sprite rifle;
    private Sprite rightHand, leftHand;
    private Sprite rightArm;
    private Sprite leftArm, leftForearm;

    // UBAH INI: Jadi Array untuk menampung banyak variasi flash
    private Sprite[] muzzleFlashes;
    private int activeFlashIndex = 0;
    private Vector3 mouseCoordinates;
    private float currentAngle = 0;
    private float walkTimer = 0;
    private boolean isMoving = false;
    private float flashTimer = 0f;
    private int maxHealth;
    private int currentHealth;
    private Rectangle collider;
    private float hitCd;
    private boolean isDead;


    public Player(float x, float y) {
        position = new Vector2(x, y);
        speed = 200;
        mouseCoordinates = new Vector3();

        // LOAD ASSETS
        torso = new Sprite(new Texture("torso.png"));
        head = new Sprite(new Texture("head.png"));
        rifle = new Sprite(new Texture("rifle.png"));
        rightHand = new Sprite(new Texture("hand_holding_gun.png"));
        leftHand = new Sprite(new Texture("hand_steadying_gun.png"));

        Texture armTex = new Texture("arm.png");
        Texture forearmTex = new Texture("forearm.png");

        rightArm = new Sprite(armTex);
        leftArm = new Sprite(armTex);
        leftForearm = new Sprite(forearmTex);

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
        applyScale(rightHand, scale);
        applyScale(leftHand, scale);
        applyScale(rightArm, scale);
        applyScale(leftArm, scale);
        applyScale(leftForearm, scale);

        // Scale semua flash dalam array
        for (Sprite flash : muzzleFlashes) {
            applyScale(flash, scale * 0.06f);
        }
        this.startPosition = new Vector2(x, y);
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.collider = new Rectangle(0, 0, torso.getWidth()*scale, torso.getHeight()*scale);
    }

    private void applyScale(Sprite s, float scale) {
        s.setSize(s.getWidth() * scale, s.getHeight() * scale);
        s.setOriginCenter();
    }

    public void update(float dt, OrthographicCamera cam) {
        if(hitCd>0){
            hitCd -= dt;
        }
        collider.setCenter(position.x, position.y);
        handleRotation(cam);
        updateBodyParts(dt);

        if (fireTimer > 0) {
            fireTimer -= dt;
        }

        if (flashTimer > 0) {
            flashTimer -= dt;
            if (flashTimer < 0) flashTimer = 0;
        }
    }

    public void reset(){
        currentHealth = maxHealth;
        isDead = false;
        isMoving = false;
        this.position.set(startPosition);
        this.collider.setPosition(position.x, position.y);
    }

    public void stopMoving(){
        isMoving = false;
    }

    public void moveUp(float delta){
        position.y += speed * delta;
        isMoving = true;
    }

    public void moveDown(float delta){
        position.y -= speed * delta;
        isMoving = true;
    }

    public void moveRight(float delta){
        position.x += speed *delta;
        isMoving = true;
    }

    public void moveLeft(float delta){
        position.x -= speed * delta;
        isMoving = true;
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

        // --- UPDATE DENGAN ROTASI ---

        // Kaki (Tanpa rotasi tambahan)
        setSpriteTransform(leftFoot, footSwing, 8f * scale, 0);
        setSpriteTransform(rightFoot, -footSwing, -8f * scale, 0);

        // Badan & Kepala (Tanpa rotasi tambahan)
        setSpriteTransform(torso, 0, 0, 0);
        setSpriteTransform(head, 2f * scale, 0, 0);

        // Senjata & Tangan (Tanpa rotasi tambahan, ikut body)
        setSpriteTransform(rifle, RIFLE_FORWARD * scale, RIFLE_SIDE * scale, 0);
        setSpriteTransform(rightHand, HAND_RIGHT_FORWARD * scale, HAND_RIGHT_SIDE * scale, 0);
        setSpriteTransform(leftHand, HAND_LEFT_FORWARD * scale, HAND_LEFT_SIDE * scale, 0);

        // --- LENGAN DENGAN ROTASI TAMBAHAN ---
        // Lengan Kanan: Miring sedikit (-10 derajat)
        setSpriteTransform(rightArm, ARM_RIGHT_FORWARD * scale, ARM_RIGHT_SIDE * scale, ARM_RIGHT_ANGLE);

        // Lengan Kiri: Miring tajam (-50 derajat)
        setSpriteTransform(leftArm, ARM_LEFT_FORWARD * scale, ARM_LEFT_SIDE * scale, ARM_LEFT_ANGLE);

        // Lengan Bawah Kiri: Miring (-45 derajat)
        setSpriteTransform(leftForearm, FOREARM_LEFT_FORWARD * scale, FOREARM_LEFT_SIDE * scale, FOREARM_LEFT_ANGLE);
    }

    // MODIFIKASI HELPER: Menambahkan parameter rotationOffset
    private void setSpriteTransform(Sprite s, float localX, float localY, float rotationOffset) {
        float rad = currentAngle * MathUtils.degRad;
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float globalX = position.x + (localX * cos - localY * sin);
        float globalY = position.y + (localX * sin + localY * cos);

        s.setCenter(globalX, globalY);

        // Rotasi Player + Rotasi Tambahan Lengan
        s.setRotation(currentAngle + rotationOffset);
    }

    public void render(SpriteBatch sb) {
        leftFoot.draw(sb);
        rightFoot.draw(sb);
        leftArm.draw(sb);
        leftForearm.draw(sb);
        torso.draw(sb);
        head.draw(sb);
        rightArm.draw(sb);
        rightHand.draw(sb);
        rifle.draw(sb);
        leftHand.draw(sb);

        if (flashTimer > 0) {
            Vector2 tip = getGunTipPosition();

            // --- LOGIKA FLASH BARU (MENGGUNAKAN OFFSET) ---
            float rad = currentAngle * MathUtils.degRad;
            // Hitung posisi visual flash: Tip + (Jarak Tambahan * Scale)
            float flashX = tip.x + MathUtils.cos(rad) * (FLASH_FORWARD_ADJUSTMENT * scale);
            float flashY = tip.y + MathUtils.sin(rad) * (FLASH_FORWARD_ADJUSTMENT * scale);

            Sprite activeFlash = muzzleFlashes[activeFlashIndex];
            activeFlash.setCenter(flashX, flashY); // Gunakan posisi yang sudah di-offset
            activeFlash.setRotation(currentAngle);
            activeFlash.draw(sb);
        }
    }

    public void shoot() {
        flashTimer = 0.05f;
        fireTimer = fireRate;

        // random flash
        activeFlashIndex = MathUtils.random(0, 1);
    }

    public boolean canShoot() {
        return fireTimer <= 0;
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
        rightHand.getTexture().dispose();
        leftHand.getTexture().dispose();
        rightArm.getTexture().dispose();
        leftArm.getTexture().dispose();
        leftForearm.getTexture().dispose();

        for (Sprite flash : muzzleFlashes) {
            flash.getTexture().dispose();
        }
    }

    public Vector2 getPosition(){
        return position;
    }




    @Override
    public void addObserver(Observer observer){
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String event){
        for(Observer observer : observers){
            observer.onNotify(this, event);
        }
    }

    public void takeDamage(int amount){
        if(hitCd <= 0 &&!isDead){
            currentHealth -= amount;
            hitCd = 1f;
            notifyObserver("DAMAGE");
            if(currentHealth <=0){
                isDead = true;
                notifyObserver("DEAD");
            }
        }
    }

    public void heal(int amount) {
        currentHealth += amount;

        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }

        notifyObserver("health");
    }

    public Rectangle getCollider(){
        return collider;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getMaxHealth(){
        return maxHealth;
    }
}

