package message;

public class PlaySoundMessage {
    public String soundId;

    public PlaySoundMessage() {} // Required for Kryo

    public PlaySoundMessage(String soundId) {
        this.soundId = soundId;
    }
}
