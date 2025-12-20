package com.finpro.kel10.Frontend.services;

import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.enemies.WalkerZombie;

public class WalkerPool extends ObjectPool<WalkerZombie>{
    @Override
    protected WalkerZombie createObject(){
        return new WalkerZombie(new Vector2(0, 0));
    }

    @Override
    protected void resetObject(WalkerZombie z){}
}
