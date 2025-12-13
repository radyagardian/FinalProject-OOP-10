package com.finpro.kel10.Frontend.services;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T> {
    private List<T> available = new ArrayList<>();
    private List<T> inUse = new ArrayList<>();

    protected abstract T createObject();
    protected abstract void resetObject(T object);

    public T obtain(){
        T object = null;
        if(available.isEmpty()){
            object = createObject();
        }else{
            object = available.remove(available.size()-1);
        }
        inUse.add(object);
        return object;
    }

    public void release(T object){
        if(inUse.remove(object)){
            resetObject(object);
            available.add(object);
        }
    }

    public void releaseAll(){
        for(T object : inUse){
            resetObject(object);
            available.add(object);
        }
        inUse.clear();
    }

    public List<T> getInUse(){
        return new ArrayList<>(inUse);
    }
}
