package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.projectiles.SpitProjectile;
import com.finpro.kel10.Frontend.services.ObjectPool;
import com.finpro.kel10.Frontend.services.SpitProjectilePool;

public class RunnerZombie extends BaseZombie {
    private Vector2 dashDirection;
    private float dashDuration = 0.5f;
    private float currentDashTime = 0;
    private boolean isDashing;
    private ObjectPool<SpitProjectile> projectilePool;
    private SpitProjectile spitTemp;
    private float dashCdTimer = 0;
    private float dashCdTotal = 4f;
    private boolean hasDealtDamage;
    private float idleTimer;
    private float waitTime = 2f;
    private boolean isLooping; //spit, dash, idle, repeat

    public RunnerZombie(Vector2 startPos, ObjectPool<SpitProjectile> pool) {
        super(startPos);
        this.projectilePool = pool;
        Texture img = new Texture(Gdx.files.internal("runnerZombie.png"));
        this.sprite.setRegion(img);
        this.sprite.setSize(img.getWidth(), img.getHeight());
        this.sprite.setOriginCenter();
        this.speed = 100f;
        this.health = 20;
        this.maxHealth = 20;
        this.damage = 10;
        this.scoreValue = 10;
        this.dashDirection = new Vector2();
        this.attCd = 2f;
        this.attackTimer = 0;
    }

    @Override
    public void botBehavior(float delta, Player player) {
        float angle = MathUtils.atan2(player.getPosition().y - this.position.y,
            player.getPosition().x - this.position.x) * MathUtils.radDeg;
        this.sprite.setRotation(angle - 90);

        float distance = Vector2.dst(this.position.x, this.position.y, player.getPosition().x, player.getPosition().y);

        if (idleTimer > 0){
            idleTimer -= delta;
            return;
        }

        if (isDashing) {
            this.position.mulAdd(this.dashDirection, 400f * delta);
            this.sprite.setPosition(this.position.x, this.position.y);

            if(distance < 50f && !hasDealtDamage){
                player.takeDamage(damage);
                hasDealtDamage = true;
            }

            currentDashTime -= delta;
            if (currentDashTime <= 0) {
                isDashing = false;
                idleTimer = 1.5f;
                attackTimer = 0;
            }
            return;
        }

        if(distance < 400f){
            if(attackTimer <= 0){
                if(projectilePool != null){
                    SpitProjectile bullet = projectilePool.obtain();
                    bullet.initialize(this.position, player.getPosition());
                    this.spitTemp = bullet;
                }
                this.dashDirection = player.getPosition().cpy().sub(this.position).nor();
                this.isDashing = true;
                this.currentDashTime = dashDuration;
                this.hasDealtDamage = false;
                this.attackTimer = 999;
            }
        }
        else{
            Vector2 direction = player.getPosition().cpy().sub(this.position).nor();
            this.position.mulAdd(direction, this.speed * delta);
            this.sprite.setPosition(this.position.x, this.position.y);
        }
    }

    public SpitProjectile getSpit() {
        SpitProjectile temp = spitTemp;
        spitTemp = null;
        return temp;
    }
}
