package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.enemies.BaseZombie;

import java.util.*;


public class EnemyFactory {
    public interface EnemyCreator {
        BaseZombie create(float x, float y);
        void release (BaseZombie enemy);
        void releaseAll();
        List<? extends BaseZombie> getInUse();
        boolean supports(BaseZombie enemy);
        String getName();
    }
    private final Map<String, EnemyCreator> creators = new HashMap<>();
    private final List<EnemyCreator> weightedSelection = new ArrayList<>();
    private final Random random = new Random();

    public EnemyFactory(){
        register(new WalkerCreator());
        register(new RunnerCreator());
        register(new TankCreator());
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

    public void release(BaseZombie enemy){
        for(EnemyCreator creator : creators.values()){
            if(creator.supports(enemy)){
                creator.release(enemy);
                return;
            }
        }
    }

    public void releaseAllEnemies(){
        for(EnemyCreator creator : creators.values()){
            creator.releaseAll();;
        }
    }

    public List<BaseZombie> getAllInUseEnemies(){
        List<BaseZombie> list = new ArrayList<>();
        for(EnemyCreator creator : creators.values()){
            list.addAll(creator.getInUse());
        }
        return list;
    }
}
