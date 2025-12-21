package com.finpro.kel10.Frontend;

public class GameManager {
    private static GameManager instance;
    private int score;
    private String username;

    private GameManager(){
        this.score = 0;
    }

    public static GameManager getInstance(){
        if(instance == null){
           instance = new GameManager();
        }
        return instance;
    }

    public void addScore(int amount){
        score += amount;
    }

    public int getScore(){
        return score;
    }

    public int resetScore(){
        return score = 0;
    }

    public void setUsername(String name) {
        this.username = name;
    }
    public String getUsername() {
        return this.username;
    }
}
