package com.finpro.kel10.Frontend.services;

import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.enemies.TankZombie;

public class TankPool extends ObjectPool<TankZombie> {
    @Override
    protected TankZombie createObject(){
        return new TankZombie(new Vector2(0,0));
    }

    @Override
    protected void resetObject(TankZombie z){}
}
