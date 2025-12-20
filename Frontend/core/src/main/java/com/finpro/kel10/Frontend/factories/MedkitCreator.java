package com.finpro.kel10.Frontend.factories;

import com.finpro.kel10.Frontend.entities.BaseItem;
import com.finpro.kel10.Frontend.entities.Medkit;
import com.finpro.kel10.Frontend.services.MedkitPool; // Pastikan ini adalah class Pool, bukan Manager

import java.util.List;

// Implement interface dari ItemFactory
public class MedkitCreator implements ItemFactory.ItemCreator {

    private final MedkitPool pool = new MedkitPool();

    @Override
    public BaseItem create(float x, float y) {
        Medkit medkit = pool.obtain();
        medkit.spawn(x, y); // Asumsi ada method spawn/initialize di Medkit
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
        // Pastikan MedkitPool punya method releaseAll (biasanya dari parent ObjectPool)
        // Jika pakai library libGDX Pool biasa, mungkin harus manual clear
        // Tapi mengikuti pola RunnerCreator kamu:
        pool.releaseAll(); // atau pool.releaseAll() tergantung implementasi Pool kamu
    }

    @Override
    public List<? extends BaseItem> getInUse() {
        // Ini perlu implementasi khusus di Pool kamu untuk melacak objek aktif
        // Sesuai RunnerCreator kamu yang memanggil pool.getInUse()
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
