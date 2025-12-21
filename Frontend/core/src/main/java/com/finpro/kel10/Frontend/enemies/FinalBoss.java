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
        // ngadep
        float angle = MathUtils.atan2(player.getPosition().y - this.position.y,
            player.getPosition().x - this.position.x) * MathUtils.radDeg;
        this.sprite.setRotation(angle - 90);

        // gerak
        Vector2 direction = player.getPosition().cpy().sub(this.position).nor();
        this.position.mulAdd(direction, speed * delta);
        this.sprite.setPosition(position.x, position.y);

        // Ability 1 : homing flies(?) idk
        homingTimer -= delta;
        if (homingTimer <= 0) {
            shootHoming(player);
            homingTimer = homingCd;
        }

        // Ability 2: NOVA BURST!
        burstTimer -= delta;
        if (burstTimer <= 0) {
            shootNova();
            burstTimer = burstCd;
        }
    }

    private void shootHoming(Player player) {
        HomingProjectile missile = new HomingProjectile();
        missile.initialize(this.position);
        missile.setTarget(player);

        queuedProjectiles.add(missile);
    }

    private void shootNova() {
        for (int i = 0; i < 360; i += 30) {
            SpitProjectile bullet = new SpitProjectile();

            float angleRad = i * MathUtils.degRad; // Pastikan dikali degRad!

            // target (sebenernya biar ngelurusin aja makanya jauh banget)
            Vector2 targetPos = new Vector2(
                this.position.x + MathUtils.cos(angleRad) * 1000f,
                this.position.y + MathUtils.sin(angleRad) * 1000f
            );

            bullet.initialize(this.position, targetPos);
            queuedProjectiles.add(bullet);
        }
        System.out.println("NOVA FIRED!"); // buat debug ae
    }

    public List<BaseProjectile> getProjectiles() {
        if (queuedProjectiles.isEmpty()) return null;
        List<BaseProjectile> temp = new ArrayList<>(queuedProjectiles);
        queuedProjectiles.clear();
        return temp;
    }
}
