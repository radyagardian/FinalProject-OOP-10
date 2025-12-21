package com.finpro.kel10.Frontend.strategies;

import java.util.HashMap;
import java.util.Map;

public class WaveFour implements DifficultyStrategy {
    @Override
    public float getSpawnInterval() {
        return 2.3f;
    }

    @Override
    public Map<String, Integer> getEnemyWeights() {
        Map<String, Integer> weights = new HashMap<>();
        weights.put("RunnerZombie", 100);
        return weights;
    }
    @Override
    public int getDensity(){
        return 3;
    }
}
