package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;

public class TankZombie extends BaseZombie{
    public TankZombie(Vector2 startPos){
        super(startPos);
        Texture img = new Texture(Gdx.files.internal("tankZombie.png"));
        this.sprite.setRegion(img);
        this.sprite.setSize(img.getWidth(), img.getHeight());
        this.sprite.setOriginCenter();
        this.speed = 50f;
        this.health = 200;
        this.maxHealth = 200;
        this.damage = 10;
        this.scoreValue = 10;
        sprite.setScale(1.5f);
        this.collider.setSize(img.getWidth() * 1.5f, img.getHeight() * 1.5f);
    }

    @Override
    public void botBehavior(float delta, Player player) {
        float angle = MathUtils.atan2(player.getPosition().y - this.position.y, player.getPosition().x - this.position.x) *MathUtils.radDeg;
        this.sprite.setRotation(angle - 90);
        Vector2 direction = player.getPosition().cpy().sub(this.position).nor();
        this.position.mulAdd(direction, this.speed * delta);
        this.sprite.setPosition(this.position.x, this.position.y);

        float distance = Vector2.dst(this.position.x, this.position.y, player.getPosition().x, player.getPosition().y);
        if(distance <50f && attackTimer <= 0){
            player.takeDamage(damage);
            attackTimer = 1f;
        }
    }

    @Override //increase collider size after respawn
    public void initialize(float x, float y){
        super.initialize(x, y);
        this.collider.setSize(sprite.getWidth() * 1.5f, sprite.getHeight() * 1.5f);
        this.collider.setPosition(x, y);
    }
}
