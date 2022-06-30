package io.github.azizie13.pong.entities;

import io.github.azizie13.pong.gamelogic.AIController;
import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import static io.github.azizie13.pong.gui.PongPanel.GAME_HEIGHT;

public class Paddle extends Rectangle{

    public int id;
    public float dx, dy;
    private final float ySpeed = 4f;
    private boolean stunned = false;
    public int stunTimer = 3 * 60;

    private AIController aiControl;

    private Color color;

    public Paddle(Rectangle rectangle, int id) {
        super(rectangle);
        this.id = id;
    }


    public void draw(Graphics g) {
        g.setColor(color);

        if(!(stunned && stunTimer % 30 > 15))
            g.fillRect(x, y, width, height);
    }

    private void movePaddle(){
        this.y += dy * ySpeed;

        //Stop from going off screen
        this.y = Math.min(GAME_HEIGHT - this.height, this.y);
        this.y = Math.max(0, this.y);
    }


    public void move() {
        this.dy = 0;

        if(!this.stunned){
            this.keyControl();
        }else if(stunTimer > 0){
            stunTimer--;
        }else stunned = false;

        this.movePaddle();
    }

    public void aiControl() {

        if(aiControl == null){
            aiControl = new AIController(this, PongPanel.balls.get(0));
        }

        if(!this.stunned){
            aiControl.processAI();
        }else if(stunTimer > 0){
            stunTimer--;
        }else stunned = false;

        this.movePaddle();
    }

    public AIController getAiControl() {
        return aiControl;
    }

    public void keyControl() {
        HashSet<Integer> keyIsDown = PongPanel.keyIsDown;
        switch(this.id){
            case 1:
                if(keyIsDown.contains(KeyEvent.VK_W)){
                    this.moveUp();
                }
                if(keyIsDown.contains(KeyEvent.VK_S)){
                    this.moveDown();
                }
                break;
            case 2:
                if(keyIsDown.contains(KeyEvent.VK_UP)){
                    this.moveUp();
                }
                if(keyIsDown.contains(KeyEvent.VK_DOWN)) {
                    this.moveDown();
                }
                break;
        }
    }

    public void moveUp(){
        this.dy = -ySpeed;
    }

    public void moveDown(){
        this.dy = ySpeed;
    }

    public void stopMoving(){
        this.dy = 0;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStunned(boolean flag){
        this.stunned = flag;
    }

    public boolean isStunned() {
        return stunned;
    }
}
