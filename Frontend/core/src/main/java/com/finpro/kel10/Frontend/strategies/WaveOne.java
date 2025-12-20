package com.finpro.kel10.Frontend.strategies;

import java.util.HashMap;
import java.util.Map;

public class WaveOne implements DifficultyStrategy{
    @Override
    public Map<String, Integer> getEnemyWeights(){
        Map<String, Integer> weights = new HashMap<>();
        weights.put("WalkerZombie", 100);
        return weights;
    }
    @Override
    public float getSpawnInterval(){
        return 2.0f;
    }
    @Override
    public int getDensity(){
        return 3;
    }
}
