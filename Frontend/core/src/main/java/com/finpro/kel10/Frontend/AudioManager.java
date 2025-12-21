package com.finpro.kel10.Frontend;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private Music backgroundMusic;
    private Sound gunshotSFX;

    public AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("ThemeSong.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.40F);

        gunshotSFX = Gdx.audio.newSound(Gdx.files.internal("GunShot.mp3"));
    }

    public void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public void playGunshotSFX(){
        gunshotSFX.play(0.1f);
    }

    public void dispose(){
        backgroundMusic.dispose();
        gunshotSFX.dispose();
    }
}
