package message;

public class LobbyJoinMessage {
    private int id;

    public LobbyJoinMessage() {
    }

    public LobbyJoinMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
