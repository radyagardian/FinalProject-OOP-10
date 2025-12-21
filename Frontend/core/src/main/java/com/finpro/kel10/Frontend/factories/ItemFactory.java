package com.finpro.kel10.Frontend.factories;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.finpro.kel10.Frontend.entities.BaseItem;
import com.finpro.kel10.Frontend.entities.Medkit; // Pastikan import Medkit
import com.finpro.kel10.Frontend.entities.Player;

import java.util.*;

public class ItemFactory {

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
    private float mapWidth = 1280; // Sesuaikan dengan mapWorldWidth di PlayingState
    private float mapHeight = 720; // Sesuaikan dengan mapWorldHeight

    public ItemFactory() {
        register(new MedkitCreator());
        resetSpawnTimer();
    }

    private void register(ItemCreator creator) {
        creators.put(creator.getName(), creator);
    }

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

    // --- LOGIKA UPDATE UTAMA ---
    public void update(float dt, Player player) {
        // A. Spawn Timer Logic
        timer += dt;
        if (timer >= nextSpawnTime) {
            spawnRandomItem();
            resetSpawnTimer();
        }

        // B. Update Item & Cek Collision
        // Kita gabung di sini agar efisien
        processActiveItems(dt, player);
    }

    private void resetSpawnTimer() {
        timer = 0;
        nextSpawnTime = MathUtils.random(15f, 30f); // Spawn tiap 15-30 detik
        System.out.println("Item berikutnya spawn dalam: " + nextSpawnTime + " detik");
    }

    private void spawnRandomItem() {
        if (weightedSelection.isEmpty()) return;

        int randomIndex = random.nextInt(weightedSelection.size());
        ItemCreator selectedCreator = weightedSelection.get(randomIndex);

        // Random posisi (sesuaikan batas map agar tidak spawn di luar tembok)
        float x = MathUtils.random(100, mapWidth - 100);
        float y = MathUtils.random(100, mapHeight - 100);

        BaseItem item = selectedCreator.create(x, y);
        System.out.println("Spawned: " + selectedCreator.getName() + " at " + x + "," + y);
    }

    private void processActiveItems(float dt, Player player) {
        for (ItemCreator creator : creators.values()) {
            List<? extends BaseItem> activeItems = creator.getInUse();

            for (int i = activeItems.size() - 1; i >= 0; i--) {
                BaseItem item = activeItems.get(i);

                if (!item.isActive()) {
                    creator.release(item);
                    continue;
                }

                if (item.getBounds().overlaps(player.getCollider())) {
                    if (item instanceof Medkit) {
                        player.heal(30);
                        System.out.println("Player took Medkit! HP +30");
                    }

                    // Tambahkan efek item lain (Ammo, Speed) pakai else if disini...

                    creator.release(item);
                }
            }
        }
    }

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
