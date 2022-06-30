package io.github.azizie13.pong.entities;

import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static io.github.azizie13.pong.gui.PongPanel.GAME_HEIGHT;

public class Ball extends Rectangle {
    private float dx, dy;
    private Color color;
    private float XSpeed = 15f, YSpeed = 15f;
    private int state = 0;

    public Ball(Rectangle rectangle, Color color) {
        super(rectangle);
        this.color = color;

    }

    public Ball(Ball other) {
        super(other.x, other.y, other.width, other.height);
        this.color = other.color;
        this.XSpeed = other.getYSpeed();
        this.YSpeed = other.getXSpeed();
        this.dx = other.dx;
        this.dy = other.dy;
        this.state = other.state;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public void move() {
        this.x += dx * XSpeed;
        this.y += dy * YSpeed;

        //Wall Bounce Physics
        if(this.y <= 0){
            this.dy *= -1;
        } else if(this.y >= GAME_HEIGHT - this.height){
            this.dy *= -1;
        }

        if(state == 2 && (this.x <= 0 || this.x >= PongPanel.GAME_WIDTH || this.y <= -this.height || this.y >= GAME_HEIGHT + this.height)){
            state = -1;
        }
    }

    public Boolean collide(Paddle paddle){
        if(this.intersects(paddle)){
            if(state == 1){
                System.out.println("Paddle " + paddle.id + " got stunned!");
                paddle.setStunned(true);
                paddle.stunTimer = PongPanel.random.nextInt(5) * 30;
                state = 0;
            }
            return true;
        }
        return false;
    }

    public void setXSpeed(float speed) {
        this.XSpeed = speed;
    }

    public float getXSpeed() {
        return XSpeed;
    }

    public void setYSpeed(float speed) {
        this.YSpeed = speed;
    }

    public float getYSpeed() {
        return YSpeed;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setSize(int size){
        this.height = size;
        this.width = size;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static void clearBall(){
        PongPanel.balls = (ArrayList<Ball>) PongPanel.balls.stream().filter(ball -> ball.getState() != -1).collect(Collectors.toList());
    }

    public static void addClones(){

        int randAmount = PongPanel.random.nextInt(3) + 1;
        for(int i = 0; i < randAmount; i++){
            Ball cloneBall = new Ball(PongPanel.balls.get(0));
            cloneBall.setState(2);
            cloneBall.setLocation(cloneBall.x + (PongPanel.random.nextInt(3) - 3) * PongPanel.BALL_DIAMETER, cloneBall.y + (PongPanel.random.nextInt(10) - 10) * PongPanel.BALL_DIAMETER);
            cloneBall.setColor(new Color(200,200,200));

            PongPanel.balls.add(cloneBall);
        }
    }
}
