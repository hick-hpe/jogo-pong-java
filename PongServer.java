import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class PongServer extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 800, HEIGHT = 600;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2, ballDX = 2, ballDY = 2;
    private int paddle1Y = HEIGHT / 2 - 60, paddle2Y = HEIGHT / 2 - 60;
    private int paddleWidth = 10, paddleHeight = 120;
    private int paddleSpeed = 20;
    private int score1 = 0, score2 = 0;
    private Timer timer;
    private boolean up1, down1, up2, down2;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public PongServer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        try {
            serverSocket = new ServerSocket(12345);
            clientSocket = serverSocket.accept();
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            timer = new Timer(5, this);
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, 15, 15);
        g.fillRect(30, paddle1Y, paddleWidth, paddleHeight);
        g.fillRect(WIDTH - 40, paddle2Y, paddleWidth, paddleHeight);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString(String.valueOf(score1), WIDTH / 4, 50);
        g.drawString(String.valueOf(score2), 3 * WIDTH / 4 - 50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Lê os movimentos enviados pelo cliente
            up2 = in.readBoolean();
            down2 = in.readBoolean();

            // Atualiza o movimento do jogador 2 (paddle 2)
            if (up2 && paddle2Y > 0) {
                paddle2Y -= paddleSpeed;
            }
            if (down2 && paddle2Y < HEIGHT - paddleHeight) {
                paddle2Y += paddleSpeed;
            }

            // Movimento da bola
            ballX += ballDX;
            ballY += ballDY;

            if (ballY <= 0 || ballY >= HEIGHT - 15) {
                ballDY = -ballDY;
            }

            if (ballX <= 40 && ballY + 15 >= paddle1Y && ballY <= paddle1Y + paddleHeight) {
                ballDX = -ballDX;
            }
            if (ballX >= WIDTH - 55 && ballY + 15 >= paddle2Y && ballY <= paddle2Y + paddleHeight) {
                ballDX = -ballDX;
            }

            if (ballX <= 0) {
                score2++;
                resetBall();
            }
            if (ballX >= WIDTH - 15) {
                score1++;
                resetBall();
            }

            if (up1 && paddle1Y > 0) {
                paddle1Y -= paddleSpeed;
            }
            if (down1 && paddle1Y < HEIGHT - paddleHeight) {
                paddle1Y += paddleSpeed;
            }

            // Envia o estado atualizado do jogo para o cliente
            out.writeInt(paddle1Y);
            out.writeInt(paddle2Y);
            out.writeInt(ballX);
            out.writeInt(ballY);
            out.writeInt(score1);
            out.writeInt(score2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        repaint();
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballDX = -ballDX;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            up1 = true;
        }
        if (key == KeyEvent.VK_S) {
            down1 = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            up1 = false;
        }
        if (key == KeyEvent.VK_S) {
            down1 = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Server");
        PongServer game = new PongServer();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
