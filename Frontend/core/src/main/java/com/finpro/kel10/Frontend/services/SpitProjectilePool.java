package com.finpro.kel10.Frontend.services;

import com.finpro.kel10.Frontend.projectiles.SpitProjectile;

public class SpitProjectilePool extends ObjectPool<SpitProjectile>{
    @Override
    public SpitProjectile createObject(){
        return new SpitProjectile();
    }

    @Override
    public void resetObject(SpitProjectile p){
        p.setActive(false);
    }
}
