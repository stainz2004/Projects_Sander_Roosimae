package ee.taltech.examplegame.server.listener;

public class LobbyDataMessageSender extends Thread {

    private ServerListener serverListener;

    public LobbyDataMessageSender(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                serverListener.sendLobbyDataToClients();
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
