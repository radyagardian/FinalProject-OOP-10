package com.finpro.kel10.Frontend;

public class GameManager {
    private static GameManager instance;
    private int score;
    private int killCount;
    private String username;

    private GameManager(){
        this.score = 0;
        this.killCount = 0;
        this.username = "Player";
    }

    public void addKill(){
        killCount++;
    }

    public int getKillCount(){
        return killCount;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
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

    public void resetScore(){
        score = 0;
        killCount = 0;
    }
}
