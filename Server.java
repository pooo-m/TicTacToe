import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<ReadServer> allReadThread;
    private ServerSocket serverSocket;
    private FileWriter writeInLog;
    private int countOfClient;
    public Server() throws IOException {
        countOfClient = 0;
        allReadThread = new ArrayList<ReadServer>();
        writeInLog = new FileWriter("/home/osboxes/IdeaProjects/TicTacToe/log.txt", true);
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(7777);
            while (true){
                Socket socket = serverSocket.accept();
                synchronized (allReadThread) {
                    ReadServer n = new ReadServer(socket, allReadThread, writeInLog, countOfClient);
                    allReadThread.add(n);
                    n.start();
                    n.setName("ReadThread");
                    countOfClient++;
                }
            }
        }
        catch (Exception e) {}
    }
}
