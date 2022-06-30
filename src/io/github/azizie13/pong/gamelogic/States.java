package io.github.azizie13.pong.gamelogic;

import io.github.azizie13.pong.gamestate.*;
import java.util.HashMap;

public class States {
    public static GameState currentGameState;
    private static final HashMap<String, GameState> gameStates = new HashMap<>();

    public static void newStates() {
        gameStates.put("title", new TitleScreenState("title"));
        gameStates.put("serve", new ServeState("serve"));
        gameStates.put("over", new GameOverState("over"));

        currentGameState = gameStates.get("title");
    }

    public static void changeState(String name){
        if(!gameStates.containsKey(name)){return;}
        currentGameState = gameStates.get(name);
    }
}
