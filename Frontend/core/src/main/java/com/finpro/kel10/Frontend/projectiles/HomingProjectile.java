package com.finpro.kel10.Frontend.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.entities.Player;

// EXTENDS BASEPROJECTILE (Bukan SpitProjectile lagi)
public class HomingProjectile extends BaseProjectile {

    private Player target;
    private float speed = 180f;
    private TextureRegion textureRegion;
    private static Texture sharedTexture;
    private float rotation;

    private float width = 40f;
    private float height = 40f;

    public HomingProjectile() {
        super();
        if (sharedTexture == null) {
            sharedTexture = new Texture(Gdx.files.internal("bossProjectile.png"));
        }
        this.textureRegion = new TextureRegion(sharedTexture);

        this.active = true;
        this.damage = 20;

        // Setup ukuran collider
        collider.setSize(width, height);
    }

    public void initialize(Vector2 startPos) {
        this.position.set(startPos);
        collider.setPosition(position.x, position.y);
        this.active = true;
    }

    public void setTarget(Player player) {
        this.target = player;
    }

    public void hitByPlayerBullet() {
        this.active = false;
        System.out.println("Missile Destroyed!");
    }

    @Override
    public void update(float dt) {
        if (!active) return;
        if (target == null) { active = false; return; }

        // Logic Pengejaran
        float angleToTarget = MathUtils.atan2(
            target.getPosition().y - position.y,
            target.getPosition().x - position.x
        );

        position.x += MathUtils.cos(angleToTarget) * speed * dt;
        position.y += MathUtils.sin(angleToTarget) * speed * dt;

        this.rotation = angleToTarget * MathUtils.radDeg;

        collider.setPosition(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!active) return;
        sb.draw(textureRegion,
            position.x, position.y,
            width / 2, height / 2,
            width, height,
            1f, 1f,
            rotation);
    }
}
