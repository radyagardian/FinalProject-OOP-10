package com.finpro.kel10.Frontend.services;

import com.finpro.kel10.Frontend.projectiles.SpitProjectile;

public class SpitProjectilePool extends ObjectPool<SpitProjectile>{
    @Override
    protected SpitProjectile createObject(){
        return new SpitProjectile();
    }

    @Override
    protected void resetObject(SpitProjectile p){
        p.setActive(false);
    }
}
