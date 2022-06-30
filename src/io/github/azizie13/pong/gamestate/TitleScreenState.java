package io.github.azizie13.pong.gamestate;

import io.github.azizie13.pong.entities.Powerup;
import io.github.azizie13.pong.entities.Selection;
import io.github.azizie13.pong.gui.PongPanel;
import io.github.azizie13.pong.gamelogic.States;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class TitleScreenState extends GameState {
    public int pointer = 0;
    public boolean pressed = true, openedSetting = false;
    private final ArrayList<Selection> selections = new ArrayList<>();

    public TitleScreenState(String stateName) {
        super(stateName);

        selections.add(new Selection(0, "VS Mode: ", Color.green, new ArrayList<>(Arrays.asList("Player Vs Player", "Player Vs AI", "AI Vs AI"))));
        selections.add(new Selection(1, "Shrink Mode: ", Color.blue, new ArrayList<>(Arrays.asList("On", "Off"))));
        selections.add(new Selection(2, "Powerups: ", Color.magenta, new ArrayList<>(Arrays.asList("None","Stun", "Clone"))));
        selections.add(new Selection(3, "Paddle Colors: ", Color.yellow, new ArrayList<>(Arrays.asList("Red Vs Blue", "White Vs Yellow", "Dark Mode", "Random"))));
    }

    public void update() {
        this.openedSetting = false;
        if(PongPanel.keyIsDown.contains(KeyEvent.VK_CONTROL)){
            this.updateSettings();
            this.openedSetting = true;
        }

        if(PongPanel.keyIsDown.contains(KeyEvent.VK_SPACE)){
            this.setupSettings();
            PongPanel.generatePaddleColors();
            Powerup.counter = Powerup.spawnTimer;
            States.changeState("serve");
        }
    }

    private void updateSettings() {
        if(PongPanel.keyIsDown.contains(KeyEvent.VK_UP)){
            if(pressed){
                pressed = false;
                pointer = Math.max(0, pointer - 1);
            }
        }

        if(PongPanel.keyIsDown.contains(KeyEvent.VK_DOWN)){
            if(pressed) {
                pressed = false;
                pointer = Math.min(selections.size() - 1, pointer + 1);
            }
        }

        for(Selection selection: selections){
            if(selection.getIndex() == pointer && PongPanel.keyIsDown.contains(KeyEvent.VK_RIGHT)){
                if(pressed) {
                    pressed = false;
                    selection.setOption(Math.min(selection.getOptions().size() - 1, selection.getOption() + 1));
                }
            }

            if(selection.getIndex() == pointer && PongPanel.keyIsDown.contains(KeyEvent.VK_LEFT)){
                if(pressed) {
                    pressed = false;
                    selection.setOption(Math.max(0, selection.getOption() - 1));
                }
            }
        }

        if(!PongPanel.keyIsDown.contains(KeyEvent.VK_DOWN)
                && !PongPanel.keyIsDown.contains(KeyEvent.VK_UP)
                && !PongPanel.keyIsDown.contains(KeyEvent.VK_RIGHT)
                && !PongPanel.keyIsDown.contains(KeyEvent.VK_LEFT)){
            pressed = true;
        }
    }


    private void setupSettings() {
        for(Selection selection : selections){
            switch (selection.getOptions().get(selection.getOption())){
                case "Player Vs Player":
//                    System.out.println("PvP Mode");
                    ServeState.setAIFlag1(false);
                    ServeState.setAIFlag2(false);
                    break;
                case "Player Vs AI":
//                    System.out.println("PvE Mode");
                    ServeState.setAIFlag1(false);
                    ServeState.setAIFlag2(true);
                    break;
                case "AI Vs AI":
//                    System.out.println("AI Mode");
                    ServeState.setAIFlag1(true);
                    ServeState.setAIFlag2(true);
                    break;
                case "On":
//                    System.out.println("Shrink Mode: On");
                    ServeState.setBallSizeFactor(0.9f);
                    break;
                case "Off":
//                    System.out.println("Shrink Mode: Off");
                    ServeState.setBallSizeFactor(1.0f);
                    break;
                case "None":
//                    System.out.println("Powerup: None");
                      ServeState.powerupFlag = 0;
                    break;
                case "Stun":
//                    System.out.println("Powerup: Bomb");
                      ServeState.powerupFlag = 1;
                    break;
                case "Clone":
                    System.out.println("Powerup: Clone");
                    ServeState.powerupFlag = 2;

                    ArrayList<Integer> clearIndex = new ArrayList<Integer>();


                    break;
                case "Red Vs Blue":
//                    System.out.println("Red VS Blue");
                    PongPanel.paddleColorFlag = 1;
                    break;
                case "White Vs Yellow":
//                    System.out.println("White Vs Yellow");
                    PongPanel.paddleColorFlag = 2;
                    break;
                case "Dark Mode":
//                    System.out.println("Dark Mode");
                    PongPanel.paddleColorFlag = 3;
                    break;
                case "Random":
//                    System.out.println("Random");
                    PongPanel.paddleColorFlag = 0;
                    break;
            }
        }
    }

    public void draw(Graphics g) {
        PongPanel.paddle1.draw(g);
        PongPanel.paddle2.draw(g);
        PongPanel.score.draw(g);

        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 120));
        g.drawString("PONG", PongPanel.GAME_WIDTH/2 - 135, PongPanel.GAME_HEIGHT/2);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Consolas", Font.PLAIN, 50));
        g.drawString("Press SPACE to begin.", PongPanel.GAME_WIDTH/2 - 250, PongPanel.GAME_HEIGHT/2 + 50);

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString("Hold CTRL to adjust settings.", PongPanel.GAME_WIDTH/2 - 120, PongPanel.GAME_HEIGHT/2 + 200);

        if(this.openedSetting){
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            if(pointer != 0)
                g.drawString("^", PongPanel.GAME_WIDTH/2 - 270, PongPanel.GAME_HEIGHT/2 + 290);

            if(pointer != selections.size() - 1)
                g.drawString("v", PongPanel.GAME_WIDTH/2 - 270, PongPanel.GAME_HEIGHT/2 + 300);

            for(Selection selection: selections){
                if(selection.getIndex() == pointer){
                    g.setColor(selection.color);
                    g.setFont(new Font("Consolas", Font.PLAIN, 30));
                    g.drawString(selection.getText(), PongPanel.GAME_WIDTH/2 - 250, PongPanel.GAME_HEIGHT/2 + 300);
                    g.drawString("< " + selection.getOptions().get(selection.getOption()) + " >", PongPanel.GAME_WIDTH/2 - 230, PongPanel.GAME_HEIGHT/2 + 350);
                }
            }
        }
    }
}
