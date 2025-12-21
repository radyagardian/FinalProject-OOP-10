package com.finpro.kel10.Frontend.factories;

import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.enemies.BossZombie;
// Hapus import ObjectPool dan SpitProjectile yang tidak dipakai lagi disini

import java.util.*;

public class EnemyFactory {

    public interface EnemyCreator {
        BaseZombie create(float x, float y);
        void release(BaseZombie enemy);
        void releaseAll();
        List<? extends BaseZombie> getInUse();
        boolean supports(BaseZombie enemy);
        String getName();
    }

    private final Map<String, EnemyCreator> creators = new HashMap<>();
    private final List<EnemyCreator> weightedSelection = new ArrayList<>();
    private final Random random = new Random();

    // Variable ObjectPool DIHAPUS karena tidak diperlukan lagi

    public EnemyFactory(){
        // Tidak perlu inisialisasi pool lagi

        register(new WalkerCreator());
        register(new RunnerCreator());
        register(new TankCreator());
        register(new BossCreator());
    }

    private void register(EnemyCreator creator){
        creators.put(creator.getName(), creator);
    }

    public void setWeights(Map<String, Integer> weights){
        weightedSelection.clear();
        for(Map.Entry<String, Integer> entry : weights.entrySet()){
            String name = entry.getKey();
            int weight = entry.getValue();
            if(creators.containsKey(name)){
                for(int i=0; i<weight;i++){
                    weightedSelection.add(creators.get(name));
                }
            }
        }
    }

    public BaseZombie createRandomEnemy(float x, float y){
        if(weightedSelection.isEmpty()){
            return null;
        }
        EnemyCreator creator = selectWeightedCreator();
        return creator.create(x, y);
    }

    private EnemyCreator selectWeightedCreator(){
        int randomIndex = random.nextInt(weightedSelection.size());
        return weightedSelection.get(randomIndex);
    }

    public void release(BaseZombie enemy)   {
        for(EnemyCreator creator : creators.values()){
            if(creator.supports(enemy)){
                creator.release(enemy);
                return;
            }
        }
    }

    public void releaseAllEnemies(){
        for(EnemyCreator creator : creators.values()){
            creator.releaseAll();
        }
    }

    public List<BaseZombie> getAllInUseEnemies(){
        List<BaseZombie> list = new ArrayList<>();
        for(EnemyCreator creator : creators.values()){
            list.addAll(creator.getInUse());
        }
        return list;
    }

    // --- BOSS CREATOR (Tanpa Pool) ---
    private class BossCreator implements EnemyCreator {
        private List<BossZombie> activeBosses = new ArrayList<>();

        @Override
        public BaseZombie create(float x, float y) {
            // Cukup panggil constructor BossZombie(vector)
            BossZombie boss = new BossZombie(new Vector2(x, y));
            activeBosses.add(boss);
            return boss;
        }

        @Override
        public void release(BaseZombie enemy) {
            activeBosses.remove(enemy);
        }

        @Override
        public void releaseAll() {
            activeBosses.clear();
        }

        @Override
        public List<? extends BaseZombie> getInUse() {
            return activeBosses;
        }

        @Override
        public boolean supports(BaseZombie enemy) {
            return enemy instanceof BossZombie;
        }

        @Override
        public String getName() {
            return "Boss";
        }
    }

    public BaseZombie createBoss(float x, float y) {
        // Pembuatan boss secara manual juga jadi simpel
        return new BossZombie(new Vector2(x, y));
    }
}
