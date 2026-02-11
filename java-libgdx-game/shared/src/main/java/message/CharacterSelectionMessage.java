package message;

public class CharacterSelectionMessage {

    // id-d ei vaja kliendi poolt määramist, server genereerib automaatselt
    private int id;
    private int playerNumber;

    // Vaikimisi konstruktor
    public CharacterSelectionMessage() {}

    // Konstruktor, mis seadistab ainult playerNumber
    public CharacterSelectionMessage(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    // Kui vajad ka edaspidi oma konstruktorit, võid jätta selle alles
    public CharacterSelectionMessage(int id, int playerNumber) {
        this.id = id;
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer number) {
        playerNumber = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
