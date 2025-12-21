package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.entities.BaseItem;
import com.finpro.kel10.Frontend.entities.Medkit;
import com.finpro.kel10.Frontend.services.MedkitPool;

import java.util.List;

// Implement interface dari ItemFactory
public class MedkitCreator implements ItemFactory.ItemCreator {

    private final MedkitPool pool = new MedkitPool();

    @Override
    public BaseItem create(float x, float y) {
        Medkit medkit = pool.obtain();
        medkit.spawn(x, y);
        return medkit;
    }

    @Override
    public void release(BaseItem item) {
        if (item instanceof Medkit) {
            pool.release((Medkit) item);
        }
    }

    @Override
    public void releaseAll() {
        pool.releaseAll();
    }

    @Override
    public List<? extends BaseItem> getInUse() {
        return pool.getInUse();
    }

    @Override
    public boolean supports(BaseItem item) {
        return item instanceof Medkit;
    }

    @Override
    public String getName() {
        return "Medkit";
    }
}
