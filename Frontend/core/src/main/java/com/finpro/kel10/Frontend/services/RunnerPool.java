package com.finpro.kel10.Frontend.services;

import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.enemies.RunnerZombie;

public class RunnerPool extends ObjectPool<RunnerZombie> {
    private SpitProjectilePool spitPool;

    public RunnerPool(SpitProjectilePool spitPool){
        this.spitPool = spitPool;
    }

    @Override
    protected RunnerZombie createObject(){
        return new RunnerZombie(new Vector2(0,0), this.spitPool);
    }

    @Override
    protected void resetObject(RunnerZombie z){}
}
