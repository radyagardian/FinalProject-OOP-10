package com.finpro.kel10.Frontend;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private Music backgroundMusic;
    private Sound gunshotSFX;
    private Sound gameoverSFX;
    private Sound wavechangeSFX;

    public AudioManager() {
        // Theme song
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("ThemeSong.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.40F);

        // Gunshot
        gunshotSFX = Gdx.audio.newSound(Gdx.files.internal("GunShot.mp3"));

        // Gameover
        gameoverSFX = Gdx.audio.newSound(Gdx.files.internal("GameOver.mp3"));

        // ganti wave
        wavechangeSFX = Gdx.audio.newSound(Gdx.files.internal("WaveChange.mp3"));
    }

    public void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public void stopBackgroundMusic(){
        if (backgroundMusic.isPlaying()){
            backgroundMusic.stop();
        }
    }

    public void pauseBackgroundMusic(){
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resumeBackgroundMusic() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void playGunshotSFX(){
        gunshotSFX.play(0.1f);
    }

    public void playGameoverSFX(){
        gameoverSFX.play(0.6f);
    }

    public void playWaveChangeSFX(){
        wavechangeSFX.play(0.6f);
    }

    public void dispose(){
        backgroundMusic.dispose();
        gunshotSFX.dispose();
        gameoverSFX.dispose();
        wavechangeSFX.dispose();
    }
}
