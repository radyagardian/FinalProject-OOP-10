package com.finpro.kel10.Frontend.services;


import com.finpro.kel10.Frontend.entities.Bullet;

public class BulletPool extends ObjectPool<Bullet>{
    @Override
    protected Bullet createObject(){
        return new Bullet();
    }

    @Override
    protected void resetObject(Bullet b){
        b.setActive(false);
    }
}
