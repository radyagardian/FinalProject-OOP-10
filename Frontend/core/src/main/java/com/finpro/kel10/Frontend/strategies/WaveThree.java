package com.finpro.kel10.Frontend.strategies;

import java.util.HashMap;
import java.util.Map;

public class WaveThree implements DifficultyStrategy{
    @Override
    public Map<String, Integer> getEnemyWeights(){
        Map<String, Integer> weights = new HashMap<>();
        weights.put("WalkerZombie", 40);
        weights.put("RunnerZombie", 40);
        weights.put("TankZombie", 20);
        return weights;
    }
    @Override
    public float getSpawnInterval(){
        return 1.5f;
    }
    @Override
    public int getDensity(){
        return 5;
    }
}
