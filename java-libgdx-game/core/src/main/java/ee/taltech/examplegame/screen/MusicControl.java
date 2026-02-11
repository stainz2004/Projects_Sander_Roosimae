package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicControl {
    private static MusicControl instance;
    private Music currentMusic;
    Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("tacata.mp3"));
    private float musicVolume = 1f;
    private float soundVolume = 1f;
    private Sound buttonPressSound;
    private Sound gameStartSound;

    private MusicControl() {
        buttonPressSound = Gdx.audio.newSound(Gdx.files.internal("buttonPress.mp3"));
        gameStartSound = Gdx.audio.newSound(Gdx.files.internal("readyFight.mp3"));
        // Private constructor so no one can create new instances
    }

    public static MusicControl getInstance() {
        if (instance == null) {
            instance = new MusicControl();
        }
        return instance;
    }
    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void playMusic(Music music) {
        stopMusic(); // Stop previous music if playing
        currentMusic = music;
        currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    public float getMusicVolume() {
        if (currentMusic != null) {
            return currentMusic.getVolume();
        }
        return 0f;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public void increaseMusicVolume(float volume) {
        if (currentMusic != null) {
            float lastVolume = currentMusic.getVolume();
            currentMusic.setVolume(Math.min(lastVolume + volume, 1f)); // limit max to 1.0
        }
    }

    public void decreaseMusicVolume(float volume) {
        if (currentMusic != null) {
            float lastVolume = currentMusic.getVolume();
            currentMusic.setVolume(Math.max(lastVolume - volume, 0f)); // minimum 0
        }
    }
    public void playSoundById(String soundId) {
        switch (soundId) {
            case "button_press":
                buttonPressSound.play(1f);
                break;
            case "game_start":
                gameStartSound.play(8f);
                break;
            default:
                System.out.println("Unknown sound ID: " + soundId);
        }
    }

    public void setMusicLooping(boolean looping) {
        if (currentMusic != null) {
            currentMusic.setLooping(looping);
        }
    }

    public void playSound(Sound sound) {
        sound.play(soundVolume);
    }
    public void playSound(Sound sound, float pitch) {
        long id = sound.play(soundVolume);
        sound.setPitch(id, pitch);
    }


    public void setSoundVolume(Sound sound, float volume, long id) {
        sound.setVolume(id, volume);
    }

    public void disposeSound(Sound sound) {
        sound.dispose();
    }
}
