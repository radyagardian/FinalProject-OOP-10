package com.finpro.kel10.Frontend.observers;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(String event);
}
