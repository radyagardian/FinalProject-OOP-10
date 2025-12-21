package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Medkit extends BaseItem {

    public Medkit() {
        super();
        Texture tex = new Texture("health-red.png");
        sprite = new Sprite(tex);
        sprite.setSize(50, 50);

        // hitbox
        bounds.setSize(50, 50);
    }

    @Override
    public void onPickup(Player player) {
        System.out.println("Player healed!");
    }
}
