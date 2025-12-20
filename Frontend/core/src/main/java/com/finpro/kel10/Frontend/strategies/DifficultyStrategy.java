package com.finpro.kel10.Frontend.strategies;

import java.util.Map;

public interface DifficultyStrategy {
    Map<String, Integer> getEnemyWeights();
    float getSpawnInterval();
    int getDensity();
}
