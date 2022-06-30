package io.github.azizie13.pong.gamestate;

import java.awt.*;

public abstract class GameState {
    String stateName;

    public GameState(String stateName) {
        this.stateName = stateName;
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    public String getStateName() {
        return stateName;
    }
}
