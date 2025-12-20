package com.finpro.kel10.Frontend;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;

public class AudioManager {
    private Music backgroundMusic;

    public AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("ThemeSong.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.40F);
    }

    public void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public void dispose(){
        backgroundMusic.dispose();
    }
}
