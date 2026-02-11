package ee.taltech.examplegame.network.listener;

public class PlaySoundMessage {
    public String soundId;

    public PlaySoundMessage() {} // empty constructor for Kryonet serialization

    public PlaySoundMessage(String soundId) {
        this.soundId = soundId;
    }
}
