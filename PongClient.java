import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class PongClient extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 800, HEIGHT = 600;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2, ballDX = 2, ballDY = 2;
    private int paddle1Y = HEIGHT / 2 - 60, paddle2Y = HEIGHT / 2 - 60;
    private int paddleWidth = 10, paddleHeight = 120;
    private int paddleSpeed = 20;
    private int score1 = 0, score2 = 0;
    private Timer timer;
    private boolean up1, down1, up2, down2;

    private Socket socket;
    private DataInputStream in;

    public PongClient() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        try {
            socket = new Socket("192.168.246.101", 12345);  // Conecta ao servidor
            in = new DataInputStream(socket.getInputStream());
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
            // Lê as informações enviadas pelo servidor
            paddle1Y = in.readInt();
            paddle2Y = in.readInt();
            ballX = in.readInt();
            ballY = in.readInt();
            score1 = in.readInt();
            score2 = in.readInt();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            up2 = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            down2 = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            up2 = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            down2 = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Client");
        PongClient game = new PongClient();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
