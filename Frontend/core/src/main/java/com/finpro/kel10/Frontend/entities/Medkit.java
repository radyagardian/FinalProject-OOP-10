package com.finpro.kel10.Frontend.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Medkit extends BaseItem {

    public Medkit() {
        super(); // Panggil constructor BaseItem

        // Load asset
        Texture tex = new Texture("health-red.png");
        sprite = new Sprite(tex);
        sprite.setSize(50, 50);

        // Set ukuran hitbox
        bounds.setSize(50, 50);
    }

    @Override
    public void onPickup(Player player) {
        // Logika healing nanti dimasukkan di sini
        System.out.println("Player healed!");
        // player.heal(10); // Contoh jika nanti ada method heal
    }
}
