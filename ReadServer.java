import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadServer extends Thread {
    private BufferedReader in;
    private PrintWriter myOut;
    private FileWriter writeInLog;
    private Socket socket;
    DateFormat dateFormat;
    private ArrayList<ReadServer> allReadThread;
    private ReadServer hisServer;
    private boolean searchPair;
    private volatile String figure;
    private String[][] desk;
    private boolean exitGame;
    private int myNumber;

    public ReadServer(Socket s, ArrayList<ReadServer> all, FileWriter w, int c) throws Exception {
        socket = s;
        allReadThread = all;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        myOut = new PrintWriter(socket.getOutputStream(), true);
        desk = new String[10][10];
        searchPair = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                desk[i][j] = "0";
            }
        }
        exitGame = false;
        myNumber = c;
        writeInLog = w;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        synchronized (writeInLog) {
            writeInLog.write("" + dateFormat.format(date) + " Client № " + myNumber + " connect with server\n");
            writeInLog.flush();
        }
        myOut.println("@okey");
    }

    public int getMyNumber() {
        return myNumber;
    }

    public boolean getExitGame() {
        return exitGame;
    }

    public PrintWriter getPrint() {
        return myOut;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setHisServer(ReadServer s) {
        hisServer = s;
    }

    public boolean getSearchPair() {
        return searchPair;
    }

    public void setSearchPair(boolean b) {
        searchPair = b;
    }

    public void setFigure(boolean f) {
        if (f) figure = "X";
        else figure = "O";
    }

    public void setHisStep(int n) {
        if (figure.equals("X"))
            desk[n / 10][n % 10] = "O";
        else desk[n / 10][n % 10] = "X";
    }

    public boolean chekWin() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!desk[i][j].equals(figure)) {
                    count = 0;
                    break;
                } else count++;
            }
            if (count == 10) return true;
        }
        count = 0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (!desk[i][j].equals(figure)) {
                    count = 0;
                    break;
                } else count++;
            }
            if (count == 10) return true;
        }
        count = 0;
        for (int i = 0; i < 10; i++) {
            if (!desk[i][i].equals(figure)) {
                count = 0;
                break;
            } else count++;
        }
        if (count == 10) return true;
        count = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 9; j >= 0; j--) {
                if (!desk[i][j].equals(figure)) {
                    count = 0;
                    break;
                } else count++;
            }
            if (count == 10) return true;
        }
        return false;
    }

    public void setNullDesk() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                desk[i][j] = "0";
            }
        }
    }

    public void sendAnswer(String request) throws IOException {
        myOut = new PrintWriter(socket.getOutputStream(), true);
        if (request.equals("@newgame")) {
            searchPair = true;
            myOut.println("@waitpair");
            synchronized (writeInLog) {
                Date date = new Date();
                writeInLog.write(""+ dateFormat.format(date) + "Client № " + myNumber + " start new game\n");
                writeInLog.flush();
            }
        } else if (request.equals("@existgame")) {
            myOut.println("@waitpair");
            searchPair = true;
            int i = 0;
            while (searchPair) {
                synchronized (allReadThread) {
                    if (!searchPair) {
                        synchronized (writeInLog) {
                            Date date = new Date();
                            writeInLog.write(""+ dateFormat.format(date)+"Client № " + myNumber + " start exist game\n");
                            writeInLog.flush();
                        }
                        return;
                    }
                    while (i < allReadThread.size()) {
                        ReadServer otherServer = allReadThread.get(i);
                        if (otherServer == this || otherServer == null) {
                            i++;
                            continue;
                        }
                        //System.out.println(i);
                        if (otherServer.getSearchPair()) {
                            synchronized (writeInLog) {
                                Date date = new Date();
                                writeInLog.write(""+ dateFormat.format(date)+"Client № " + myNumber + " start exist game\n");
                                writeInLog.flush();
                            }
                            otherServer.setSearchPair(false);
                            searchPair = false;
                            hisServer = otherServer;
                            hisServer.setHisServer(this);
                            figure = "X";
                            hisServer.setFigure(false);
                            if (socket.isConnected()) {
                                myOut.println("@startgame true");
                                myOut.flush();
                            }
                            if (hisServer.getSocket().isConnected()) {
                                hisServer.getPrint().println("@startgame false");
                                hisServer.getPrint().flush();
                            }
                            break;
                        }
                        i++;
                    }
                    i = 0;
                }
            }
        } else if (request.contains("@step")) {
            String words[] = request.split(" ");
            byte n = (byte) Integer.parseInt(words[1]);
            desk[n / 10][n % 10] = figure;
            hisServer.setHisStep(n);
            hisServer.getPrint().println("@hisstep " + words[1]);
            boolean win = chekWin();
            if (win) {
                myOut.println("@win");
                hisServer.getPrint().println("@lose");
                synchronized (writeInLog) {
                    Date date = new Date();
                    writeInLog.write(""+ dateFormat.format(date)+"Client № " + myNumber + " win \n");
                    writeInLog.write(""+ dateFormat.format(date)+"Client № " + hisServer.getMyNumber() + " lose \n");
                }
                hisServer.setNullDesk();
                hisServer.setHisServer(null);
                hisServer = null;
                setNullDesk();
            }
        } else if (request.equals("@giveup")) {
            setNullDesk();
            synchronized (writeInLog) {
                writeInLog.write("Client № " + hisServer.getMyNumber() + " win \n");
                writeInLog.write("Client № " + myNumber + " lose \n");
                writeInLog.flush();
            }
            hisServer.getPrint().println("@win");
            hisServer.setNullDesk();
            hisServer.setHisServer(null);
            hisServer = null;
        } else if (request.equals("@exit")) {
            synchronized (writeInLog) {
                writeInLog.write("Client № " + myNumber + " left the game\n");
                writeInLog.flush();
            }
            myOut.println("@exit");
            if (hisServer != null) {
                hisServer.getPrint().println("@win");
                hisServer = null;
            }
            closeConnection();
            allReadThread.remove(this);
        }
    }

    public void closeConnection() throws IOException {
        searchPair = false;
        myOut = null;
        in = null;
        hisServer = null;
        setNullDesk();
        socket.close();
    }

    public void run() {
        try {
            while (!exitGame) {
                String message = in.readLine();
                if (message == null) continue;
                sendAnswer(message);
            }

        } catch (IOException e) {
        } catch (NullPointerException e2) {
        }

    }
}
