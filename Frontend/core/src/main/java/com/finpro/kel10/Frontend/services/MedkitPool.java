package com.finpro.kel10.Frontend.services;

import com.finpro.kel10.Frontend.entities.Medkit;

public class MedkitPool extends ObjectPool<Medkit> {

    public MedkitPool() {
    }

    @Override
    protected Medkit createObject() {
        return new Medkit();
    }

    @Override
    protected void resetObject(Medkit m) {
    }
}
