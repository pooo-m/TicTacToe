package View;
import Model.InfAboutDesk;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class InterfaceOfGame extends JFrame {
    private InfAboutDesk model;
    private Socket socket;
    private PrintWriter out;
    private ArrayList<JButton> desk;
    private JFrame jFrame;
    private JPanel gamePanel;
    private JPanel des;
    private JPanel mainPanel;
    private JPanel waitServerPanel;
    private JPanel waitPlayerPanel;
    private JButton giveUp;
    private JButton newGame;
    private JButton existingGame;
    private JButton goToMenu;
    private JLabel waitServer;
    private JLabel waitPlayer;
    private JLabel whoStep;
    private JLabel playersAndFigure1;
    private JLabel playersAndFigure2;
    private JLabel resultOfGame;

    public InterfaceOfGame(InfAboutDesk m, Socket s)
            throws IOException {
        model = m;
        socket = s;
        out = new PrintWriter(socket.getOutputStream(), true);
        jFrame = new JFrame();
        jFrame.setResizable(false);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jFrame.setSize(820, 550);
        jFrame.setLocationRelativeTo(null);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                super.windowClosed(windowEvent);
                out.println("@exit");
            }
        });

        initWaitServerPanel();
        initWaitPlayersPanel();
        initMainPanel();
        initGamePanel();
    }

    //панель ожидания сервера
    public void initWaitServerPanel() {
        waitServerPanel = new JPanel(new BorderLayout());
        waitServerPanel.setSize(450, 100);
        waitServerPanel.add(waitServer = new JLabel("There is no connection to the server..."), BorderLayout.CENTER);
        waitServer.setFont(new Font("Serif", Font.PLAIN, 20));
        waitServerPanel.setLocation(120, 200);
        jFrame.add(waitServerPanel);
    }

    //панель ожидания поиска игрока
    public void initWaitPlayersPanel() {
        waitPlayerPanel = new JPanel(new BorderLayout());
        waitPlayerPanel.setSize(420, 100);
        waitPlayerPanel.add(waitPlayer = new JLabel("Please wait for the opponent to connect..."), BorderLayout.CENTER);
        waitPlayer.setFont(new Font("Serif", Font.PLAIN, 20));
        waitPlayerPanel.setLocation(180, 200);
        jFrame.add(waitPlayerPanel);
        waitPlayerPanel.setVisible(false);
    }


    //главная панель
    public void initMainPanel() {
        mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setSize(200, 200);
        mainPanel.add(newGame = new JButton("START NEW GAME"));
        mainPanel.add(existingGame = new JButton("START EXISTING GAME"));
        mainPanel.setLocation(300, 170);
        newGame.setFocusable(false);
        existingGame.setFocusable(false);
        newGame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                out.println("@newgame");
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        existingGame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                out.println("@existgame");
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        jFrame.add(mainPanel);
        mainPanel.setVisible(false);
    }


    //игровая панель
    public void initGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setSize(800, 800);
        gamePanel.setLocation(-5, 20);
        //gamePanel.setLayout(new FlowLayout());
        des = new JPanel();
        des.setLayout(new GridBagLayout());
        des.setSize(800, 800);
        //des.setLocation(0, 0);
        // gamePanel.setMinimumSize(new Dimension(300, 300));
        desk = new ArrayList<>();
        GridBagConstraints grid = new GridBagConstraints();
        grid.fill = GridBagConstraints.NONE;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton k = new JButton();
                k.setFont(new Font("Serif", Font.PLAIN, 20));
                grid.gridy = i;
                grid.gridx = j;
                k.setMinimumSize(new Dimension(50, 50));
                k.setMaximumSize(new Dimension(50, 50));
                k.setPreferredSize(new Dimension(50, 50));
                k.setName("" + (i * 10 + j));
                k.setOpaque(true);
                k.setBackground(new Color(0, 0, 0, 0));
                k.setContentAreaFilled(false);
                k.setFocusable(false);
                k.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        String s = k.getText();
                        if (model.getIsGame() & model.getIsMyStep() & (!k.getText().equals("O") || !k.getText().equals("X"))) {
                            boolean f = model.getFigure();
                            if (f) k.setText("X");
                            else k.setText("O");
                            out.println("@step " + k.getName());
                            whoStep.setText("Opponent's move!");
                            model.setIsMyStep(false);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                    }
                });
                desk.add(k);
                des.add(k, grid);
            }
        }
        grid.gridy = 1;
        grid.gridx = 11;
        grid.insets = new Insets(0, -30, 0, 0);
        des.add(playersAndFigure1 = new JLabel(""), grid);
        playersAndFigure1.setFont(new Font("Serif", Font.PLAIN, 20));
        grid.gridy = 2;
        grid.gridx = 11;
        grid.insets = new Insets(0, 40, 0, 0);
        des.add(playersAndFigure2 = new JLabel(""), grid);
        playersAndFigure2.setFont(new Font("Serif", Font.PLAIN, 20));
        grid.gridy = 5;
        grid.gridx = 11;
        grid.gridwidth = 2;
        grid.gridheight = 2;
        grid.insets = new Insets(0, 67, 0, 0);
        des.add(whoStep = new JLabel(""), grid);
        whoStep.setFont(new Font("Serif", Font.PLAIN, 20));
        gamePanel.add(des);
        grid.gridy = 5;
        grid.gridx = 11;
        grid.insets = new Insets(0, 55, 0, 0);
        des.add(resultOfGame = new JLabel(""), grid);
        resultOfGame.setFont(new Font("Serif", Font.PLAIN, 20));
        grid.gridy = 6;
        grid.gridx = 11;
        grid.gridwidth = 2;
        grid.gridheight = 2;
        grid.insets = new Insets(0, 47, 0, 0);
        des.add(giveUp = new JButton("I want to give up :("), grid);
        giveUp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                out.println("@giveup");
                winAndLosePanel(false);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        giveUp.setFont(new Font("Serif", Font.PLAIN, 18));
        giveUp.setMinimumSize(new Dimension(220, 50));
        giveUp.setMaximumSize(new Dimension(220, 50));
        giveUp.setPreferredSize(new Dimension(220, 50));
        giveUp.setVisible(false);
        grid.gridy = 6;
        grid.gridx = 11;
        grid.gridwidth = 2;
        grid.gridheight = 2;
        grid.insets = new Insets(0, 50, 0, 0);
        des.add(goToMenu = new JButton("Main Menu"), grid);
        goToMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                gamePanel.setVisible(false);
                mainPanel.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        goToMenu.setFocusable(false);
        goToMenu.setFont(new Font("Serif", Font.PLAIN, 20));
        goToMenu.setMinimumSize(new Dimension(200, 50));
        goToMenu.setMaximumSize(new Dimension(200, 50));
        goToMenu.setPreferredSize(new Dimension(200, 50));
        goToMenu.setVisible(false);
        gamePanel.add(des);
        jFrame.add(gamePanel);
        gamePanel.setVisible(false);
        //открыть главное окно
        jFrame.setVisible(true);
        waitServerPanel.setVisible(true); //открыть окно ожидания подключения к серверу
    }

    public String getMyFigure() {
        if (model.getFigure())
            return "X";
        else return "O";
    }

    public String getFigureOpponent() {
        if (model.getFigure())
            return "O";
        else return "X";
    }

    public void showGamePanel() {
        waitPlayerPanel.setVisible(false);
        gamePanel.setVisible(true);
    }

    public void showMainPanel() {
        waitServerPanel.setVisible(false);
        mainPanel.setVisible(true);
        jFrame.setVisible(true);
    }

    public void showWaitPairPanel() {
        mainPanel.setVisible(false);
        waitPlayerPanel.setVisible(true);
    }

    public void showHisStep(int n) {
        desk.get(n).setText(getFigureOpponent());
    }

    public void showGameFigureInf() {
        playersAndFigure1.setFont(new Font("Serif", Font.PLAIN, 20));
        playersAndFigure2.setFont(new Font("Serif", Font.PLAIN, 20));
        playersAndFigure1.setText("Your figure: " + getMyFigure());
        playersAndFigure2.setText("Opponent's figure: " + getFigureOpponent());
        giveUp.setVisible(true);
        whoStep.setVisible(true);
        resultOfGame.setVisible(false);
        resultOfGame.setText("");
        goToMenu.setVisible(false);
    }

    public void showWhoseStep(boolean f) {
        if (f) {
            whoStep.setText("Your move!");
        } else whoStep.setText("Opponents' move!");
    }

    public void winAndLosePanel(boolean r) {
        if (r) {
            resultOfGame.setText("You win! :)");
        } else resultOfGame.setText("You lose :(");
        whoStep.setVisible(false);
        goToMenu.setVisible(true);
        giveUp.setVisible(false);
        resultOfGame.setVisible(true);
    }

    public void clearDesk() {
        for (int i = 0; i < 100; i++) {
            desk.get(i).setText("");
        }
    }

}
