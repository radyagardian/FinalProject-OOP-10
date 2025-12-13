package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.enemies.WalkerZombie;
import com.finpro.kel10.Frontend.services.WalkerPool;

import java.util.List;

public class WalkerCreator implements EnemyFactory.EnemyCreator{
    private final WalkerPool pool = new WalkerPool();

    @Override
    public BaseZombie create(float x, float y){
        WalkerZombie walker = pool.obtain();
        walker.initialize(x, y);
        return walker;
    }

    @Override
    public void release(BaseZombie obstacle){
        if(obstacle instanceof WalkerZombie){
            pool.release((WalkerZombie) obstacle);
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
        if(obstacle instanceof  WalkerZombie){
            return true;
        }
        return false;
    }

    @Override
    public String getName(){
        return "WalkerZombie";
    }
}
