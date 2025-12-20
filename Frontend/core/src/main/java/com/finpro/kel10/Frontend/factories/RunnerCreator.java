package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.enemies.RunnerZombie;
import com.finpro.kel10.Frontend.services.RunnerPool;
import com.finpro.kel10.Frontend.services.SpitProjectilePool;

import java.util.List;

public class RunnerCreator implements EnemyFactory.EnemyCreator{
    private final SpitProjectilePool spitPool = new SpitProjectilePool();
    private final RunnerPool pool = new RunnerPool(spitPool);

    @Override
    public BaseZombie create(float x, float y){
        RunnerZombie runner = pool.obtain();
        runner.initialize(x, y);
        return runner;
    }

    @Override
    public void release(BaseZombie obstacle){
        if(obstacle instanceof RunnerZombie){
            pool.release((RunnerZombie) obstacle);
        }
    }

    @Override
    public void releaseAll(){
        pool.releaseAll();
        spitPool.releaseAll();
    }

    @Override
    public List<? extends BaseZombie> getInUse(){
        return pool.getInUse();
    }

    @Override
    public boolean supports(BaseZombie obstacle){
        if(obstacle instanceof  RunnerZombie){
            return true;
        }
        return false;
    }

    @Override
    public String getName(){
        return "RunnerZombie";
    }
}
