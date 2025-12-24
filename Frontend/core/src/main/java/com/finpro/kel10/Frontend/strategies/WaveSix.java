package com.finpro.kel10.Frontend.strategies;

import java.util.HashMap;
import java.util.Map;

public class WaveSix implements DifficultyStrategy {
    @Override
    public float getSpawnInterval() {
        return 2f;
    }

    @Override
    public Map<String, Integer> getEnemyWeights() {
        Map<String, Integer> weights = new HashMap<>();
        weights.put("RunnerZombie", 50);
        weights.put("WalkerZombie", 50);
        return weights;
    }
    @Override
    public int getDensity(){
        return 3;
    }
}
