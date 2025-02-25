import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 800, HEIGHT = 600;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2, ballDX = 2, ballDY = 2;
    private int paddle1Y = HEIGHT / 2 - 60, paddle2Y = HEIGHT / 2 - 60;
    private int paddleWidth = 10, paddleHeight = 120;
    private int paddleSpeed = 20;
    private int score1 = 0, score2 = 0;
    private Timer timer;
    private boolean up1, down1, up2, down2;

    public PongGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        timer = new Timer(5, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha a bola
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, 15, 15);

        // Desenha as paddles
        g.fillRect(30, paddle1Y, paddleWidth, paddleHeight);
        g.fillRect(WIDTH - 40, paddle2Y, paddleWidth, paddleHeight);

        // Desenha a linha central
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // Desenha o placar
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(score1), WIDTH / 4, 50);
        g.drawString(String.valueOf(score2), 3 * WIDTH / 4 - 50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Atualiza a posição da bola
        ballX += ballDX;
        ballY += ballDY;

        // Verifica colisão com o topo e o fundo
        if (ballY <= 0 || ballY >= HEIGHT - 15) {
            ballDY = -ballDY;
        }

        // Verifica colisão com as paddles
        if (ballX <= 40 && ballY + 15 >= paddle1Y && ballY <= paddle1Y + paddleHeight) {
            ballDX = -ballDX;
        }
        if (ballX >= WIDTH - 55 && ballY + 15 >= paddle2Y && ballY <= paddle2Y + paddleHeight) {
            ballDX = -ballDX;
        }

        // Verifica se a bola saiu da tela
        if (ballX <= 0) {
            // Jogador 2 marcou um ponto
            score2++;
            resetBall();
        }
        if (ballX >= WIDTH - 15) {
            // Jogador 1 marcou um ponto
            score1++;
            resetBall();
        }

        // Movimenta as paddles
        if (up1 && paddle1Y > 0) {
            paddle1Y -= paddleSpeed;
        }
        if (down1 && paddle1Y < HEIGHT - paddleHeight) {
            paddle1Y += paddleSpeed;
        }
        if (up2 && paddle2Y > 0) {
            paddle2Y -= paddleSpeed;
        }
        if (down2 && paddle2Y < HEIGHT - paddleHeight) {
            paddle2Y += paddleSpeed;
        }

        repaint();
    }

    // Reseta a posição da bola para o centro
    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballDX = -ballDX; // Inverte a direção da bola
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
        if (key == KeyEvent.VK_W) {
            up1 = false;
        }
        if (key == KeyEvent.VK_S) {
            down1 = false;
        }
        if (key == KeyEvent.VK_UP) {
            up2 = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            down2 = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        PongGame game = new PongGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
