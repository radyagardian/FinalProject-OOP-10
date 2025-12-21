package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.projectiles.BaseProjectile;
import com.finpro.kel10.Frontend.projectiles.HomingProjectile;
import com.finpro.kel10.Frontend.projectiles.SpitProjectile;

import java.util.ArrayList;
import java.util.List;

public class FinalBoss extends BaseZombie {

    private List<BaseProjectile> queuedProjectiles = new ArrayList<>();

    private float burstTimer = 0;
    private float burstCd = 4.0f; // Cooldown Nova

    private float homingTimer = 0;
    private float homingCd = 2.0f; // Cooldown Homing

    public FinalBoss(Vector2 startPos) {
        super(startPos);

        // Setup Visual Boss
        Texture img = new Texture(Gdx.files.internal("boss2.png"));
        this.sprite.setRegion(img);

        this.sprite.setSize(img.getWidth() * 2.0f, img.getHeight() * 2.0f);
        this.sprite.setOriginCenter();

        this.speed = 30f;
        this.maxHealth = 1000;
        this.health = maxHealth;
        this.damage = 50;
        this.scoreValue = 5000;
        this.active = true;

        this.collider.setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void botBehavior(float delta, Player player) {
        // Rotasi ke player
        float angle = MathUtils.atan2(player.getPosition().y - this.position.y,
            player.getPosition().x - this.position.x) * MathUtils.radDeg;
        this.sprite.setRotation(angle - 90);

        // Gerak ke player
        Vector2 direction = player.getPosition().cpy().sub(this.position).nor();
        this.position.mulAdd(direction, speed * delta);
        this.sprite.setPosition(position.x, position.y);

        // Ability 1: Homing Missile
        homingTimer -= delta;
        if (homingTimer <= 0) {
            shootHoming(player);
            homingTimer = homingCd;
        }

        // Ability 2: Nova Burst
        burstTimer -= delta;
        if (burstTimer <= 0) {
            shootNova();
            burstTimer = burstCd;
        }
    }

    private void shootHoming(Player player) {
        HomingProjectile missile = new HomingProjectile();
        missile.initialize(this.position); // Initialize cukup posisi
        missile.setTarget(player);

        queuedProjectiles.add(missile); // Bisa masuk karena dia anak BaseProjectile
    }

    private void shootNova() {
        for (int i = 0; i < 360; i += 30) {
            SpitProjectile bullet = new SpitProjectile();
            // ... logic sama ...
            queuedProjectiles.add(bullet); // Bisa masuk karena dia anak BaseProjectile
        }
    }

    // GANTI RETURN TYPE
    public List<BaseProjectile> getProjectiles() {
        if (queuedProjectiles.isEmpty()) return null;
        List<BaseProjectile> temp = new ArrayList<>(queuedProjectiles);
        queuedProjectiles.clear();
        return temp;
    }
}
