package Client;
import Model.InfAboutDesk;
import Controller.ConnectionWithServer;
import View.InterfaceOfGame;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InfAboutDesk infAboutDesk;
    private InterfaceOfGame interfaceOfGame;
    private ConnectionWithServer connectionWithServer;

    public Client() throws IOException {
        socket = new Socket("localhost", 7777);
        infAboutDesk = new InfAboutDesk();
        interfaceOfGame = new InterfaceOfGame(infAboutDesk, socket);
        connectionWithServer = new ConnectionWithServer(infAboutDesk, interfaceOfGame, socket);
    }

    public void startClient() {
        connectionWithServer.start();
    }
}
