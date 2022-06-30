package io.github.azizie13.pong.entities;

import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Powerup extends Paddle{
    public static int counter = 0, spawnTimer = 15 * 60;
    public static boolean cloneFlag = false;

    public static Map<Integer, Color> colorMap;
    static {
        colorMap = new HashMap<>();
        colorMap.put(0, null);
        colorMap.put(1, Color.ORANGE);
        colorMap.put(2, Color.GREEN);
    }

    public Powerup(Rectangle rectangle, int id) {
        super(rectangle, id);
    }

    public void effectGame(){
        switch(this.id){
            case 1:
                PongPanel.powerup = null;
//                System.out.println("Bomb Power Triggered");
                PongPanel.balls.get(0).setState(1);
                return;
            case 2:
                PongPanel.powerup = null;
//                System.out.println("Clone Power Triggered");
                cloneFlag = true;
        }
    }

    public Boolean collide(Ball ball){
        return this.intersects(ball);
    }
}
