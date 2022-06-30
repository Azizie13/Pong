package io.github.azizie13.pong.gui;

import io.github.azizie13.pong.entities.Ball;
import io.github.azizie13.pong.entities.Paddle;
import io.github.azizie13.pong.entities.Powerup;
import io.github.azizie13.pong.entities.Score;
import io.github.azizie13.pong.gamelogic.States;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PongPanel extends JPanel implements Runnable{
    public static final int GAME_WIDTH = 1640, GAME_HEIGHT = (int) (GAME_WIDTH * (5f/9));
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);

    public static final HashSet<Integer> keyIsDown = new HashSet<>();
    public static Boolean mouseClicked = false;
    public static final Dimension mousePos = new Dimension();

    public static final int BALL_DIAMETER = 45, PADDLE_WIDTH = 30, PADDLE_HEIGHT= 120;

    public static Thread gameThread;
    public static Image image;
    public static Graphics graphics;
    public static Random random;
    public static Paddle paddle1, paddle2;
    public static int paddleColorFlag = 0;

    public static ArrayList<Ball> balls = new ArrayList<>();
    public static ArrayList<Ball> fakeBalls = new ArrayList<>();
    public static Powerup powerup;

    public static Score score;

    public PongPanel() {
        random = new Random();
        newPaddles();
        newBall();
        States.newStates();

        score = new Score();
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.addMouseListener(new ML());
        this.addMouseMotionListener(new MM());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1e9 / amountOfTicks;
        double delta = 0;

        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1){
                this.update();
                this.repaint();

                delta--;
            }
        }
    }

    public static void newBall(){
        balls.clear();
        Rectangle rect = new Rectangle();
        rect.setLocation((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER));
        rect.setSize(BALL_DIAMETER, BALL_DIAMETER);

        Ball gameBall = new Ball(rect, Color.WHITE);

        int randomXdir = random.nextInt(2);
        if(randomXdir == 0){randomXdir--;}
        gameBall.setDx(randomXdir);

        int randomYdir = random.nextInt(2);
        if(randomYdir == 0){randomYdir--;}
        gameBall.setDy(randomYdir);

        balls.add(gameBall);
    }

    public static void newPaddles(){
        Rectangle rect = new Rectangle();
        rect.setLocation(0, (GAME_HEIGHT / 2) - PADDLE_HEIGHT / 2);
        rect.setSize(PADDLE_WIDTH, PADDLE_HEIGHT);

        paddle1 = new Paddle(rect, 1);

        rect.setLocation(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT/2) - (PADDLE_HEIGHT/2));
        paddle2 = new Paddle(rect, 2);

        generatePaddleColors();
    }

    public static void generatePaddleColors(){
        switch(paddleColorFlag){
            default:
                paddle1.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                paddle2.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                return;
            case 1:
                paddle1.setColor(Color.RED);
                paddle2.setColor(Color.BLUE);
                return;
            case 2:
                paddle1.setColor(Color.WHITE);
                paddle2.setColor(Color.YELLOW);
                return;
            case 3:
                paddle1.setColor(new Color(20,15,15));
                paddle2.setColor(new Color(20,15,15));
        }
    }

    public void paint(Graphics g){
        image = createImage(GAME_WIDTH, GAME_HEIGHT);
        graphics = image.getGraphics();

        this.draw(graphics);
        g.drawImage(image, 0, 0, this);

    }

    public void draw(Graphics g){
        States.currentGameState.draw(g);
    }

    public void update(){
        States.currentGameState.update();
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            keyIsDown.add(e.getKeyCode());
        }
        public void keyReleased(KeyEvent e){
            keyIsDown.remove(e.getKeyCode());
        }
    }

    public class ML extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            mouseClicked = true;
        }

        public void mouseReleased(MouseEvent e) {
            mouseClicked = false;
        }
    }

    public class MM extends MouseMotionAdapter {

        public void mouseMoved(MouseEvent e) {
            mousePos.setSize(e.getX(), e.getY());
        }
    }

}
