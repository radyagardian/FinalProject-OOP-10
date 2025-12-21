package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.projectiles.SpitProjectile;

import java.util.ArrayList;
import java.util.List;

public class BossZombie extends BaseZombie {
    private List<SpitProjectile> queuedSpits = new ArrayList<>();

    public BossZombie(Vector2 startPos) {
        super(startPos);

        Texture img = new Texture(Gdx.files.internal("boss1.png"));
        this.sprite.setRegion(img);

        this.sprite.setSize(img.getWidth() * 1.5f, img.getHeight() * 1.5f);
        this.sprite.setOriginCenter();

        this.speed = 40f;
        this.health = 350;
        this.maxHealth = 350;
        this.damage = 25;
        this.scoreValue = 100;
        this.attCd = 2.5f;
        this.active = true;

        this.collider.setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void botBehavior(float delta, Player player) {
        float angleToPlayer = MathUtils.atan2(player.getPosition().y - this.position.y,
            player.getPosition().x - this.position.x) * MathUtils.radDeg;
        this.sprite.setRotation(angleToPlayer - 90);

        float distance = Vector2.dst(this.position.x, this.position.y, player.getPosition().x, player.getPosition().y);

        if (distance < 500f) {
            if (attackTimer <= 0) {
                shootShotgun(player);
                attackTimer = attCd;
            }
        }

        if (distance > 100f) {
            Vector2 direction = player.getPosition().cpy().sub(this.position).nor();
            this.position.mulAdd(direction, this.speed * delta);
            this.sprite.setPosition(this.position.x, this.position.y);
        }
    }

    private void shootShotgun(Player player) {
        float baseAngle = MathUtils.atan2(
            player.getPosition().y - this.position.y,
            player.getPosition().x - this.position.x
        );

        float[] spreadAngles = {-30f, -15f, 0f, 15f, 30f};

        for (float spread : spreadAngles) {
            // --- PERUBAHAN DISINI ---
            // Langsung buat instance baru (new), tidak pakai pool
            SpitProjectile bullet = new SpitProjectile();

            float finalAngle = baseAngle + (spread * MathUtils.degRad);

            Vector2 spreadTarget = new Vector2(
                this.position.x + MathUtils.cos(finalAngle) * 1000f,
                this.position.y + MathUtils.sin(finalAngle) * 1000f
            );

            bullet.initialize(this.position, spreadTarget);
            queuedSpits.add(bullet);
        }
        System.out.println("BOSS USED SHOTGUN!");
    }

    public List<SpitProjectile> getProjectiles() {
        if (queuedSpits.isEmpty()) return null;
        List<SpitProjectile> temp = new ArrayList<>(queuedSpits);
        queuedSpits.clear();
        return temp;
    }
}
