package com.finpro.kel10.Frontend.strategies;

import java.util.HashMap;
import java.util.Map;

public class WaveTwo implements DifficultyStrategy{
    @Override
    public Map<String, Integer> getEnemyWeights(){
        Map<String, Integer> weights = new HashMap<>();
        weights.put("WalkerZombie", 60);
        weights.put("RunnerZombie", 40);
        return weights;
    }
    @Override
    public float getSpawnInterval(){
        return 1.5f;
    }
    @Override
    public int getDensity(){
        return 4;
    }
}
