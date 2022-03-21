package Controller;
import Model.InfAboutDesk;
import View.InterfaceOfGame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionWithServer extends Thread {
    private Socket socket;
    private BufferedReader read;
    private InterfaceOfGame interfaceOfGame;
    private InfAboutDesk desk;
    private String message;

    public ConnectionWithServer(InfAboutDesk m, InterfaceOfGame v, Socket s) {
        try {
            socket = s;
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
        }
        interfaceOfGame = v;
        desk = m;
        message = new String();
    }

    public void run() {
        try {
            while (true) {
                message = read.readLine();
                if (message == null) continue;
                sendAnswer(message);
            }
        } catch (Exception e) {
            System.out.println("Error in Read!");
            this.stop();
        }
    }

    public void sendAnswer(String m) throws IOException {
        if (m.equals("@okey")) {
            interfaceOfGame.showMainPanel();//загрузить главное окно
        } else if (m.equals("@waitpair")) {
            interfaceOfGame.showWaitPairPanel();//загрузить окно ожидания пары
        } else if (m.contains("@startgame")) {
            interfaceOfGame.clearDesk();
            String words[] = m.split(" ");
            desk.setIsGame(true);
            if (words[1].equals("true")) {  //поставить фигуру
                desk.setFigure(true);
                desk.setIsMyStep(true);
                interfaceOfGame.showWhoseStep(true);
            } else {
                desk.setFigure(false);
                desk.setIsMyStep(false);
                interfaceOfGame.showWhoseStep(false);
            } //поставить другую фигуру
            interfaceOfGame.showGameFigureInf();
            interfaceOfGame.showGamePanel();
        } else if (m.contains("@hisstep")) {
            String words[] = m.split(" ");
            int n = Integer.parseInt(words[1]);
            desk.setStep(n, !desk.getFigure());
            desk.setIsMyStep(true);
            interfaceOfGame.showHisStep(n); //на доске отобразить ход противника
            interfaceOfGame.showWhoseStep(true);
        } else if (m.equals("@win") || m.equals("@lose")) {
            boolean win;
            if (m.equals("@win")) win = true;
            else win = false;
            desk.setIsMyStep(false);
            desk.setIsGame(false);
            interfaceOfGame.winAndLosePanel(win);
            desk.clearDesk();//загрузка панели
        } else if (m.equals("@exit")) {
            socket.close();
            System.exit(0);
        }
    }
}
