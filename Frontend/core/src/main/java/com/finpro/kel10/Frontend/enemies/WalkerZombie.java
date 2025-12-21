package com.finpro.kel10.Frontend.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;

public class WalkerZombie extends BaseZombie {
    public WalkerZombie(Vector2 startPos){
        super(startPos);
        Texture img = new Texture(Gdx.files.internal("walkerZombie.png"));
        this.sprite.setRegion(img);
        this.sprite.setSize(img.getWidth() - 10, img.getHeight() - 20);
        this.sprite.setOriginCenter();
        this.speed = 100f;
        this.health = 30;
        this.maxHealth = 30;
        this.damage = 10;
        this.scoreValue = 10;
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
}
