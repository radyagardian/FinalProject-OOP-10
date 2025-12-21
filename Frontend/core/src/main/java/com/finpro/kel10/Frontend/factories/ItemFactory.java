package com.finpro.kel10.Frontend.factories;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.finpro.kel10.Frontend.entities.BaseItem;
import com.finpro.kel10.Frontend.entities.Player;

import java.util.*;

public class ItemFactory {

    // Interface Internal (Sama seperti EnemyCreator)
    public interface ItemCreator {
        BaseItem create(float x, float y);
        void release(BaseItem item);
        void releaseAll();
        List<? extends BaseItem> getInUse();
        boolean supports(BaseItem item);
        String getName();
    }

    private final Map<String, ItemCreator> creators = new HashMap<>();
    private final List<ItemCreator> weightedSelection = new ArrayList<>();
    private final Random random = new Random();

    private float timer;
    private float nextSpawnTime;
    private float mapWidth = 1280;
    private float mapHeight = 720;

    public ItemFactory() {
        register(new MedkitCreator());
        resetSpawnTimer();
    }

    private void register(ItemCreator creator) {
        creators.put(creator.getName(), creator);
    }

    // --- SETUP PROBABILITAS (WAJIB DIPANGGIL DI AWAL) ---
    public void setWeights(Map<String, Integer> weights) {
        weightedSelection.clear();
        for (Map.Entry<String, Integer> entry : weights.entrySet()) {
            String name = entry.getKey();
            int weight = entry.getValue();
            if (creators.containsKey(name)) {
                for (int i = 0; i < weight; i++) {
                    weightedSelection.add(creators.get(name));
                }
            }
        }
    }

    // --- LOGIKA UPDATE (SPAWN & COLLISION) ---
    public void update(float dt, Player player) {
        // A. Hitung Mundur Waktu Spawn
        timer += dt;
        if (timer >= nextSpawnTime) {
            spawnRandomItem();
            resetSpawnTimer();
        }

        // B. Cek Tabrakan (Collision)
        checkCollisions(player);
    }

    private void resetSpawnTimer() {
        // Random antara 30 sampai 45 detik
        timer = 0;
        nextSpawnTime = MathUtils.random(30f, 45f);
        System.out.println("Item berikutnya spawn dalam: " + nextSpawnTime + " detik");
    }

    private void spawnRandomItem() {
        if (weightedSelection.isEmpty()) return;

        // Pilih Creator berdasarkan probabilitas
        int randomIndex = random.nextInt(weightedSelection.size());
        ItemCreator selectedCreator = weightedSelection.get(randomIndex);

        // Random posisi spawn
        float x = MathUtils.random(50, mapWidth - 50);
        float y = MathUtils.random(50, mapHeight - 50);

        BaseItem item = selectedCreator.create(x, y);
        System.out.println("Spawned: " + selectedCreator.getName());
    }

    private void checkCollisions(Player player) {
        // Iterate semua creator untuk cek item mereka yang sedang aktif
        for (ItemCreator creator : creators.values()) {
            List<? extends BaseItem> activeItems = creator.getInUse();

            for (BaseItem item : activeItems) {
                if (item.isActive() && item.getBounds().overlaps(player.getCollider())) {

                    item.onPickup(player); // item effect
                    creator.release(item);
                    return;
                }
            }
        }
    }

    // --- RENDER & CLEANUP ---
    public void render(SpriteBatch sb) {
        for (ItemCreator creator : creators.values()) {
            List<? extends BaseItem> activeItems = creator.getInUse();
            for (BaseItem item : activeItems) {
                if (item.isActive()) {
                    item.render(sb);
                }
            }
        }
    }

    public void releaseAllItems() {
        for (ItemCreator creator : creators.values()) {
            creator.releaseAll();
        }
    }
}
