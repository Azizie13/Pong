package io.github.azizie13.pong.gamestate;

import io.github.azizie13.pong.gamelogic.States;
import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState extends GameState{
    public GameOverState(String stateName) {
        super(stateName);
    }

    public void update() {
        if(PongPanel.keyIsDown.contains(KeyEvent.VK_ENTER)){
            States.changeState("title");
            PongPanel.score.resetScore();
        }
    }

    public void draw(Graphics g) {
        PongPanel.score.draw(g);

        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 120));
        g.drawString("GAME OVER", PongPanel.GAME_WIDTH/2 - 310, PongPanel.GAME_HEIGHT/2 - 100);

        g.setColor(Color.yellow);
        g.setFont(new Font("Consolas", Font.PLAIN, 100));
        g.drawString("Player " + PongPanel.score.getWinner() + " Won!!", PongPanel.GAME_WIDTH/2 - 370, PongPanel.GAME_HEIGHT/2 + 100);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Consolas", Font.PLAIN, 50));
        g.drawString("Press ENTER to start over.", PongPanel.GAME_WIDTH/2 - 300, PongPanel.GAME_HEIGHT/2 + 250);
    }
}
