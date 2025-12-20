package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.enemies.TankZombie;
import com.finpro.kel10.Frontend.services.TankPool;

import java.util.List;

public class TankCreator implements EnemyFactory.EnemyCreator{
    private final TankPool pool = new TankPool();

    @Override
    public BaseZombie create(float x, float y){
        TankZombie tank = pool.obtain();
        tank.initialize(x, y);
        return tank;
    }

    @Override
    public void release(BaseZombie obstacle){
        if(obstacle instanceof TankZombie){
            pool.release((TankZombie) obstacle);
        }
    }

    @Override
    public void releaseAll(){
        pool.releaseAll();
    }

    @Override
    public List<? extends BaseZombie> getInUse(){
        return pool.getInUse();
    }

    @Override
    public boolean supports(BaseZombie obstacle){
        if(obstacle instanceof TankZombie){
            return true;
        }
        return false;
    }

    @Override
    public String getName(){
        return "TankZombie";
    }
}
